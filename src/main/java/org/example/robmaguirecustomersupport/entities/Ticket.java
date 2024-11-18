package org.example.robmaguirecustomersupport.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Ticket")
public class Ticket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long ticketId;
    private String userId;
    private String subject;
    private String ticketBody;
    private Timestamp dateCreated;
    private Map<String, Attachment> attachments = new HashMap<>();

    @Id
    @Column(name = "TicketId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    @Basic
    @Column(name = "UserId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String customerName) {
        this.userId = customerName;
    }

    @Basic
    @Column(name = "Subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Basic
    @Column(name = "Body")
    public String getTicketBody() {
        return ticketBody;
    }

    public void setTicketBody(String ticketBody) {
        this.ticketBody = ticketBody;
    }

    @Basic
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
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
