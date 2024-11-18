package org.example.robmaguirecustomersupport.site;

import org.example.robmaguirecustomersupport.entities.Attachment;

import java.beans.JavaBean;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JavaBean
public class Ticket {
    private long ticketId;
    private Instant dateCreated;
    private String customerName;
    private String subject;
    private String ticketBody;
    private Map<String, Attachment> attachments = new HashMap<>();

    public Ticket(String customerName, String subject, String ticketBody, List<Attachment> attachments) {
        this.customerName = customerName;
        this.subject = subject;
        this.ticketBody = ticketBody;
        if (attachments != null && !attachments.isEmpty()) {
            final Map<String, Attachment> toAdd = attachments
                    .stream()
                    .collect(Collectors.toMap(a -> a.getName(), a -> a));
            this.attachments.putAll(toAdd);
        }
    }

    public Ticket() {
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTicketBody() {
        return ticketBody;
    }

    public void setTicketBody(String ticketBody) {
        this.ticketBody = ticketBody;
    }


    public void addAttachment(Attachment attachment) {
        attachments.put(attachment.getName(), attachment);
    }

    public int getNumberOfAttachments() {
        return attachments.size();
    }

    public Attachment getAttachment(String attachmentName) {
        return attachments.get(attachmentName);
    }

    public Map<String, Attachment> getAttachments() {
        return Collections.unmodifiableMap(attachments);
    }
}