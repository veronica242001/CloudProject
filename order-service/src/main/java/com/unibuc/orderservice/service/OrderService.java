package com.unibuc.orderservice.service;

import com.unibuc.orderservice.dto.StockResponse;
import com.unibuc.orderservice.dto.OrderDto;
import com.unibuc.orderservice.dto.TicketResponse;
import com.unibuc.orderservice.model.Order;
import com.unibuc.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderDto orderDto) {
        Order order = mapToDto(orderDto);
        // update in stock service
        changeStock(orderDto.getEventCode(), orderDto.getTicketType(), orderDto.getQuantity());
        orderRepository.save(order);
    }

    private Order mapToDto(OrderDto orderDto) {
        Order order = new Order();
        order.setEventCode(orderDto.getEventCode());
        order.setTicketType(orderDto.getTicketType());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice( BigDecimal.valueOf((long) order.getQuantity() * getPriceForTicket(order.getEventCode(), order.getTicketType())));

        return order;
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
    public Integer changeStock(String eventCode, String ticketType, Integer quantity) {
        Integer responseQuantity= webClientBuilder.build().get()
                .uri( "http://stock-service/api/stock/update/" + eventCode + "/" + ticketType + "/" + quantity)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("Error response: " + errorBody)));
                })
                .bodyToMono(Integer.class)
                .onErrorResume(throwable -> {
                    System.err.println("Error during HTTP request: " + throwable.getMessage());

                    return Mono.just(Integer.valueOf(0));
                })
                .block();
        return responseQuantity;
    }

    public Integer getPriceForTicket(String eventCode, String ticketType) {
        TicketResponse ticketResponse = webClientBuilder.build().get()
                .uri( "http://ticket-service/api/ticket/eventCodeAndType/" + eventCode + "/" + ticketType)

                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(errorBody -> Mono.error(new RuntimeException("Error response: " + errorBody)));
                })
                .bodyToMono(TicketResponse.class)
                .onErrorResume(throwable -> {
                    System.err.println("Error during HTTP request: " + throwable.getMessage());
                    TicketResponse response = new TicketResponse();
                    response.setPrice(BigDecimal.valueOf(100));
                    return Mono.just(response);
                })
                .block();

        return ticketResponse != null ? ticketResponse.getPrice().intValue() : 0;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public boolean deleteObject(Long id) {
        Optional<Order> ticket = orderRepository.findById(id);
        if (!ticket.isEmpty()) {
            orderRepository.delete(ticket.get());
            return true;
        }
        return false;
    }
}
