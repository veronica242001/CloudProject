package com.unibuc.stockservice.service;

import com.unibuc.stockservice.dto.StockRequest;
import com.unibuc.stockservice.dto.StockResponse;
import com.unibuc.stockservice.model.Stock;
import com.unibuc.stockservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    public List<StockResponse> isInStock() {
        return stockRepository.findAll().stream()
                .map(stock ->
                        StockResponse.builder()
                                .eventCode(stock.getEventCode())
                                .ticketType(stock.getTicketType())
                                .isInStock(stock.getQuantity() > 0)
                                .build()
                ).toList();
    }


    public void createStock(StockRequest stockRequest) {
        Stock stock = Stock.builder()
                .eventCode(stockRequest.getEventCode())
                .ticketType(stockRequest.getTicketType())
                .quantity(stockRequest.getQuantity())
                .build();

        stockRepository.save(stock);
        log.info("Stock {} is saved", stock.getId());
    }

    public List<Stock> getAllStocks() {
        List<Stock> stocks = stockRepository.findAll();
        return stocks;
    }

    public boolean deleteObject(Long id) {
        Optional<Stock> stock = stockRepository.findById(id);
        if (!stock.isEmpty()) {
            stockRepository.delete(stock.get());
            return true;
        }
        return false;
    }
    private StockResponse mapToStockResponse(Stock stock) {
        return StockResponse.builder()
                .eventCode(stock.getEventCode())
                .ticketType(stock.getTicketType())
                .build();
    }


    public Integer updateStock(String eventCode, String ticketType, Integer quantity) {
        Optional<Stock> optionalStock = stockRepository.findByEventCodeAndTicketType(eventCode, ticketType);

        if (optionalStock.isPresent()) {
            Stock existingStock = optionalStock.get();
            existingStock.setQuantity(existingStock.getQuantity() - quantity);  // Update the quantity

            stockRepository.save(existingStock);  // Save the updated entity back to the database
            return existingStock.getQuantity();
        }

        return 0;
    }

}
