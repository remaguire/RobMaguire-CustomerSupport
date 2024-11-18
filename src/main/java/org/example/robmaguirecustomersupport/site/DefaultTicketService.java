package org.example.robmaguirecustomersupport.site;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.robmaguirecustomersupport.entities.Attachment;
import org.example.robmaguirecustomersupport.entities.TicketEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultTicketService implements TicketService {
    @Inject
    TicketRepository ticketRepository;
    @Inject
    AttachmentRepository attachmentRepository;
    @Inject
    UserRepository userRepository;

    @Override
    @Transactional
    public List<Ticket> getAllTickets() {
        final var list = new ArrayList<Ticket>();
        ticketRepository.getAll().forEach(e -> list.add(convert(e)));
        return list;
    }

    @Override
    @Transactional
    public Ticket getTicket(long id) {
        final var entity = ticketRepository.get(id);
        return (entity == null ? null : convert(entity));
    }

    private Ticket convert(TicketEntity entity) {
        final var ticket = new Ticket();
        ticket.setTicketId(entity.getTicketId());
        ticket.setCustomerName(userRepository.get(entity.getUserId()).getUsername());
        ticket.setSubject(entity.getSubject());
        ticket.setTicketBody(entity.getTicketBody());
        ticket.setDateCreated(Instant.ofEpochMilli(entity.getDateCreated().getTime()));
        attachmentRepository.getByTicketId(entity.getTicketId()).forEach(ticket::addAttachment);
        return ticket;
    }

    @Override
    @Transactional
    public void save(Ticket ticket) {
        final var entity = new TicketEntity();
        entity.setTicketId(ticket.getTicketId());
        entity.setUserId(userRepository.getByUsername(ticket.getCustomerName()).getUserId());
        entity.setSubject(ticket.getSubject());
        entity.setTicketBody(ticket.getTicketBody());

        if (ticket.getTicketId() < 1) {
            ticket.setDateCreated(Instant.now());
            entity.setDateCreated(new Timestamp(ticket.getDateCreated().toEpochMilli()));
            ticketRepository.add(entity);
            ticket.setTicketId(entity.getTicketId());
            for (Attachment attachment : ticket.getAttachments().values()) {
                attachment.setTicketId(entity.getTicketId());
                attachmentRepository.add(attachment);
            }
        } else
            ticketRepository.update(entity);
    }

    @Override
    @Transactional
    public void deleteTicket(long id) {
        ticketRepository.deleteById(id);
    }
}
