package com.unibuc.orderservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {

    private String eventCode;
    private String ticketType; // ZONA A,B,C,D
    private String description;
    private BigDecimal price;;
}
