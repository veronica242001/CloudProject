package com.unibuc.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(value = "ticket")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ticket {

    @Id
    private String id;

    private String eventCode;
    private String ticketType; // ZONA A,B,C,D

    private String description;
    private BigDecimal price;
}
