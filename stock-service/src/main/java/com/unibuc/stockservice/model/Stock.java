package com.unibuc.stockservice.model;




import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "stock")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventCode;
    private String ticketType; // ZONA A,B,C,D
    private Integer quantity;
}
