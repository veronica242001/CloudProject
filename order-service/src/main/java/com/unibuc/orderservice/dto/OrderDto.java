package com.unibuc.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String eventCode;
    private String ticketType;
    private BigDecimal price;
    private Integer quantity;

}
