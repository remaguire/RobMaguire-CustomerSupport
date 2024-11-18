package org.example.robmaguirecustomersupport.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;


@Entity
@Table(name = "Attachment")
public class Attachment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private long attachmentId;
    private long ticketId;
    private String name;
    private String mimeType;
    private byte[] contents;

    @Id
    @Column(name = "AttachmentId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(long attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Basic
    @Column(name = "TicketId")
    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    @Basic
    @Column(name = "AttachmentName")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "MimeContentType")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Lob
    @Column(name = "Contents")
    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }
}
