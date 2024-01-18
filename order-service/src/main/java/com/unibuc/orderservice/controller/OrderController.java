package com.unibuc.orderservice.controller;

import com.unibuc.orderservice.dto.OrderDto;
import com.unibuc.orderservice.dto.StockResponse;
import com.unibuc.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderDto orderRequest) {
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }

    @GetMapping("/events")
    public ModelAndView getAllEvents() {
        ModelAndView modelAndView = new ModelAndView("events");
        List<StockResponse> stockList = orderService.getAllEvents();
        modelAndView.addObject("events", stockList);
        return modelAndView;
    }
}
