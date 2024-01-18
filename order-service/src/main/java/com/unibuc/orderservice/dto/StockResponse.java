package com.unibuc.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponse {

    private String eventCode;
    private String ticketType; // ZONA A,B,C,D
    private boolean isInStock;
}
