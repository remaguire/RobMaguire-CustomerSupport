package org.example.robmaguirecustomersupport.site;

import org.example.robmaguirecustomersupport.entities.TicketEntity;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultTicketRepository
        extends GenericJpaRepository<Long, TicketEntity>
        implements TicketRepository {
}
