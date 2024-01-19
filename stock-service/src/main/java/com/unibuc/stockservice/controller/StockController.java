package com.unibuc.stockservice.controller;

import com.unibuc.stockservice.dto.StockRequest;
import com.unibuc.stockservice.dto.StockResponse;
import com.unibuc.stockservice.model.Stock;
import com.unibuc.stockservice.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/stock")
@CrossOrigin
public class StockController {
    private final StockService stockService;

    @Autowired
    StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/new")
    public ModelAndView newStock(Model model){
        ModelAndView modelAndView = new ModelAndView("stockForm");
        model.addAttribute("stockDto", new StockRequest());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView saveStock(@Valid @ModelAttribute StockRequest stockRequest, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("stockForm");
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            modelAndView.addObject("stockDto", stockRequest);
            // Add other necessary attributes
            return modelAndView;
        }

        stockService.createStock(stockRequest);
        return new ModelAndView("redirect:/api/stock");
    }

    @GetMapping()
    public ModelAndView getAllStocks() {
        ModelAndView modelAndView = new ModelAndView("stocks");
        List<Stock> stockList = stockService.getAllStocks();
        modelAndView.addObject("stocks",stockList);
        return modelAndView;
    }

    @RequestMapping ("/delete/{id}")
    public ModelAndView deleteStock(@PathVariable Long id) {
        stockService.deleteObject(id);
        return new ModelAndView("redirect:/api/stock");
    }

    @GetMapping ("/update/{eventCode}/{ticketType}/{quantity}")
    public Integer updateStock(@PathVariable String eventCode, @PathVariable String ticketType, @PathVariable Integer quantity) {
        return stockService.updateStock( eventCode, ticketType, quantity);

    }

    @GetMapping("/all")
    public ResponseEntity<List<StockResponse>> getAllEvents() {
        List<StockResponse> stockResponses = stockService.isInStock();
        return ResponseEntity.ok(stockResponses);
    }
}
