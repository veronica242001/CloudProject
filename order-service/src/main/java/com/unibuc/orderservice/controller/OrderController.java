package com.unibuc.orderservice.controller;

import com.unibuc.orderservice.dto.OrderDto;
import com.unibuc.orderservice.dto.StockResponse;
import com.unibuc.orderservice.model.Order;
import com.unibuc.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping()
    public ModelAndView getAllOrders() {
        ModelAndView modelAndView = new ModelAndView("orders");
        List<Order> orderList = orderService.getAllOrders();
        modelAndView.addObject("orders", orderList);
        return modelAndView;
    }

    @GetMapping("/new/{eventCode}/{ticketType}")
    public ModelAndView newTicket(Model model, @PathVariable String eventCode, @PathVariable String ticketType){
        ModelAndView modelAndView = new ModelAndView("orderForm");
        OrderDto orderDto = new OrderDto();
        orderDto.setEventCode(eventCode);
        orderDto.setTicketType(ticketType);
        model.addAttribute("orderDto", orderDto);
        return modelAndView;
    }

    @RequestMapping ("/delete/{id}")
    public ModelAndView deleteOrder(@PathVariable Long id) {
        orderService.deleteObject(id);
        return new ModelAndView("redirect:/api/order");
    }

    @PostMapping
    public ModelAndView saveTicket(@Valid @ModelAttribute OrderDto orderDto, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("orderForm");
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            modelAndView.addObject("orderDto", orderDto);
            return modelAndView;
        }

        orderService.placeOrder(orderDto);
        return new ModelAndView("redirect:/api/order");
    }

    @GetMapping("/events")
    public ModelAndView getAllEvents() {
        ModelAndView modelAndView = new ModelAndView("events");
        List<StockResponse> stockList = orderService.getAllEvents();
        modelAndView.addObject("events", stockList);
        return modelAndView;
    }
}
