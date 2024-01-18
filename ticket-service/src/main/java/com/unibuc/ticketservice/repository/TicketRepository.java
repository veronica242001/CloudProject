package com.unibuc.ticketservice.repository;

import com.unibuc.ticketservice.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {
}
