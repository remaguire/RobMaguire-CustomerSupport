package org.example.robmaguirecustomersupport.site;

import org.example.robmaguirecustomersupport.entities.Attachment;

public interface AttachmentRepository extends GenericRepository<Long, Attachment> {
    Iterable<Attachment> getByTicketId(long ticketId);
}
