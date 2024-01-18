package com.unibuc.stockservice;

import com.unibuc.stockservice.model.Stock;
import com.unibuc.stockservice.repository.StockRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class StockServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(StockServiceApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner loadData(StockRepository stockRepository) {
//        return args -> {
//            Stock stock = new Stock();
//            stock.setProductCode("iphone_13");
//            stock.setQuantity(100);
//
//            Stock stock1 = new Stock();
//            stock1.setProductCode("iphone_13_red");
//            stock1.setQuantity(0);
//
//            stockRepository.save(stock);
//            stockRepository.save(stock1);
//        };
//    }
}
