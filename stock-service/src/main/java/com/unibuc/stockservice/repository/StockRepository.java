package com.unibuc.stockservice.repository;

import com.unibuc.stockservice.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByEventCodeAndTicketType(String eventCode, String ticketType);
}
