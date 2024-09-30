package org.example.robmaguirecustomersupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttachmentTest {
    Attachment attachment;

    @BeforeEach
    void setup() {
        attachment = new Attachment();
    }

    @Test
    void testName() {
        assertNull(attachment.getName());
        attachment.setName("Foo Bar");
        assertEquals("Foo Bar", attachment.getName());
    }

    @Test
    void testContents() {
        assertNull(attachment.getContents());
        byte[] testContents = "This is a test.".getBytes();
        attachment.setContents(testContents);
        assertEquals(testContents, attachment.getContents());
    }
}