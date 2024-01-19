package com.unibuc.ticketservice.service;

import com.unibuc.ticketservice.dto.TicketDto;
import com.unibuc.ticketservice.dto.TicketRequest;
import com.unibuc.ticketservice.dto.TicketResponse;
import com.unibuc.ticketservice.model.Ticket;
import com.unibuc.ticketservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;

    public void createTicket(TicketRequest ticketRequest) {
        Ticket ticket = Ticket.builder()
                .eventCode(ticketRequest.getEventCode())
                .ticketType(ticketRequest.getTicketType())
                .description(ticketRequest.getDescription())
                .price(ticketRequest.getPrice())
                .build();

        ticketRepository.save(ticket);
        log.info("Ticket {} is saved", ticket.getId());
    }

    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        return tickets.stream().map(this::mapToTicketResponse).toList();
    }

    public boolean deleteObject(String id) {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (!ticket.isEmpty()) {
            ticketRepository.delete(ticket.get());
            return true;
        }
        return false;
    }
    private TicketResponse mapToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .eventCode(ticket.getEventCode())
                .ticketType(ticket.getTicketType())
                .description(ticket.getDescription())
                .price(ticket.getPrice())
                .build();
    }

    public TicketDto getTicketByEventCodeAndType(String eventCode, String ticketType) {
        Ticket ticket = ticketRepository.getByEventCodeAndTicketType(eventCode, ticketType);
        return TicketDto.builder()
                .eventCode(ticket.getEventCode())
                .ticketType(ticket.getTicketType())
                .description(ticket.getDescription())
                .price(ticket.getPrice())
                .build();

    }
}
