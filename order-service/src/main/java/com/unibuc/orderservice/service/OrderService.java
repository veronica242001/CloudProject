package com.unibuc.orderservice.service;

import com.unibuc.orderservice.dto.StockResponse;
import com.unibuc.orderservice.dto.OrderItemsDto;
import com.unibuc.orderservice.dto.OrderDto;
import com.unibuc.orderservice.model.Order;
import com.unibuc.orderservice.model.OrderItem;
import com.unibuc.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderDto orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = orderRequest.getOrderItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderItemList(orderItems);

        List<String> eventCodes = order.getOrderItemList().stream()
                .map(OrderItem::getEventCode)
                .toList();

        StockResponse[] stockResponseArray = webClientBuilder.build().get()
                .uri("http://stock-service/api/stock",
                        uriBuilder -> uriBuilder.queryParam("eventCode", eventCodes).build())
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("Error response: " + errorBody)));
                })
                .bodyToMono(StockResponse[].class)
                .onErrorResume(throwable -> {
                    System.err.println("Error during HTTP request: " + throwable.getMessage());
                    return Mono.just(new StockResponse[0]);
                })
                .block();


        boolean allProductsInStock = Arrays.stream(stockResponseArray)
                .allMatch(StockResponse::isInStock);

        if(allProductsInStock){
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    private OrderItem mapToDto(OrderItemsDto orderItemsDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(orderItemsDto.getPrice());
        orderItem.setQuantity(orderItemsDto.getQuantity());
        orderItem.setEventCode(orderItemsDto.getEventCode());
        return orderItem;
    }

    public List<StockResponse> getAllEvents() {
        StockResponse[] stockResponseArray = webClientBuilder.build().get()
                .uri("http://stock-service/api/stock/all")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("Error response: " + errorBody)));
                })
                .bodyToMono(StockResponse[].class)
                .onErrorResume(throwable -> {
                    System.err.println("Error during HTTP request: " + throwable.getMessage());
                    return Mono.just(new StockResponse[0]);
                })
                .block();

        return stockResponseArray != null ? Arrays.asList(stockResponseArray) : new ArrayList<>();
    }
}
