package org.example.robmaguirecustomersupport.site;

import jakarta.transaction.Transactional;

import java.util.List;

public interface TicketService {
    @Transactional
    List<Ticket> getAllTickets();

    @Transactional
    Ticket getTicket(long id);

    @Transactional
    void save(Ticket ticket);

    @Transactional
    void deleteTicket(long id);
}
