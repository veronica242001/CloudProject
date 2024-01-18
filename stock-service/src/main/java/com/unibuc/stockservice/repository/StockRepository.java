package com.unibuc.stockservice.repository;

import com.unibuc.stockservice.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
