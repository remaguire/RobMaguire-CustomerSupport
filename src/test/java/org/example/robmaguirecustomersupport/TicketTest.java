package org.example.robmaguirecustomersupport;

import org.example.robmaguirecustomersupport.entities.Attachment;
import org.example.robmaguirecustomersupport.site.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {
    Attachment attach1, attach2, attach3;
    List<Attachment> attachments;
    Ticket emptyTicket, ticket;

    @BeforeEach
    void setup() {
        attach1 = new Attachment();
        attach1.setName("One");
        attach1.setContents("Foo".getBytes());

        attach2 = new Attachment();
        attach2.setName("Two");
        attach2.setContents("Bar".getBytes());

        attach3 = new Attachment();
        attach3.setName("Three");
        attach3.setContents("Zam".getBytes());

        attachments = new ArrayList<>();
        attachments.add(attach1);
        attachments.add(attach2);

        emptyTicket = new Ticket();
        ticket = new Ticket("Brent Spiner", "Blah", "Foo", attachments);
    }

    @Test
    void testAttachments() {
        assertTrue(emptyTicket.getAttachments().isEmpty());
        assertEquals(0, emptyTicket.getNumberOfAttachments());

        emptyTicket.addAttachment(attach1);
        assertFalse(emptyTicket.getAttachments().isEmpty());
        assertEquals(1, emptyTicket.getNumberOfAttachments());
        assertEquals(attach1, emptyTicket.getAttachment("One"));

        assertEquals(2, ticket.getNumberOfAttachments());
        ticket.addAttachment(attach3);
        assertEquals(3, ticket.getNumberOfAttachments());
        assertEquals("Three", ticket.getAttachment("Three").getName());
        assertEquals("Zam", new String(ticket.getAttachment("Three").getContents()));

        final Map<String, Attachment> testData = new HashMap<>();
        testData.put(attach1.getName(), attach1);
        assertIterableEquals(testData.entrySet(), emptyTicket.getAttachments().entrySet());

        testData.put(attach2.getName(), attach2);
        testData.put(attach3.getName(), attach3);
        for (String key : testData.keySet())
            assertNotNull(ticket.getAttachment(key));
        for (String key : ticket.getAttachments().keySet())
            assertNotNull(testData.get(key));
    }

    @Test
    void testCustomerName() {
        assertNull(emptyTicket.getCustomerName());
        emptyTicket.setCustomerName("Foo Bar");
        assertEquals("Foo Bar", emptyTicket.getCustomerName());
        assertEquals("Brent Spiner", ticket.getCustomerName());
    }

    @Test
    void testSubject() {
        assertNull(emptyTicket.getSubject());
        emptyTicket.setSubject("Seven");
        assertEquals("Seven", emptyTicket.getSubject());
        assertEquals("Blah", ticket.getSubject());
    }

    @Test
    void testTicketBody() {
        assertNull(emptyTicket.getTicketBody());
        emptyTicket.setTicketBody("Octagon");
        assertEquals("Octagon", emptyTicket.getTicketBody());
        assertEquals("Foo", ticket.getTicketBody());
    }
}