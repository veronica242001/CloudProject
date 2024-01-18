package com.unibuc.ticketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {

    private String eventCode;
    private String ticketType; // ZONA A,B,C,D
    private String description;
    private BigDecimal price;
}
