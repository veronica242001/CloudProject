package com.unibuc.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRequest {

    private String eventCode;
    private String ticketType; // ZONA A,B,C,D
    private Integer quantity;
}
