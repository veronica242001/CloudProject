package com.unibuc.ticketservice.controller;

import com.unibuc.ticketservice.dto.TicketRequest;
import com.unibuc.ticketservice.dto.TicketResponse;
import com.unibuc.ticketservice.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
//@CrossOrigin
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @GetMapping("/new")
    public ModelAndView newTicket(Model model){
        ModelAndView modelAndView = new ModelAndView("ticketForm");
        model.addAttribute("ticketDto", new TicketRequest());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView saveTicket(@Valid @ModelAttribute TicketRequest ticketRequest, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("ticketForm");
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            modelAndView.addObject("ticketDto", ticketRequest);
            // Add other necessary attributes
            return modelAndView;
        }

        ticketService.createTicket(ticketRequest);
        return new ModelAndView("redirect:/api/ticket");
    }

    @GetMapping()
    public ModelAndView getAllTickets() {
        ModelAndView modelAndView = new ModelAndView("tickets");
        List<TicketResponse> ticketList = ticketService.getAllTickets();
        modelAndView.addObject("tickets",ticketList);
        return modelAndView;
    }

    @RequestMapping ("/delete/{id}")
    public ModelAndView deleteTransportation(@PathVariable String id) {
        ticketService.deleteObject(id);
        return new ModelAndView("redirect:/api/ticket");
    }
}
