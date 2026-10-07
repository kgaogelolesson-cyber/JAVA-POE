package com.poe.part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests using the Part 2 test data (Test Case 1 and Message 2).
 */
public class MessageTest {

    private static final String MSG1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String MSG2 = "Hi Keegan, did you receive the payment?";

    @TempDir
    Path tempDir;

    private Message message1;
    private Message message2;

    @BeforeEach
    void setUp() {
        Message.resetSession();
        Message.setStorageFile(tempDir.resolve("messages.json"));
        message1 = new Message("0012345678", 0, "+27718693002", MSG1);
        message2 = new Message("0198765432", 1, "08575975889", MSG2);
    }

    // Message length
    @Test
    void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    void testMessageLengthExactly250IsAllowed() {
        Message m = new Message("0012345678", 0, "+27718693002", "a".repeat(250));
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    void testMessageLengthFailure() {
        Message m = new Message("0012345678", 0, "+27718693002", "a".repeat(260));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", m.checkMessageLength());
    }

    // Recipient number
    @Test
    void testRecipientCellSuccess() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    void testRecipientCellFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    // Message hash
    @Test
    void testMessageHashTestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "0012345678|0|Hi Mike, can you join us for dinner tonight?|00:0:HITONIGHT",
            "0198765432|1|Hi Keegan, did you receive the payment?|01:1:HIPAYMENT",
            "2312345678|2|Hi, thanks|23:2:HITHANKS",
            "5512345678|3|Hello|55:3:HELLOHELLO"
    })
    void testMessageHashLoop(String id, int number, String text, String expected) {
        assertEquals(expected, new Message(id, number, "+27718693002", text).createMessageHash());
    }

    // Message ID
    @Test
    void testMessageIDCreated() {
        Message m = new Message(0, "+27718693002", MSG1);
        assertEquals(10, m.getMessageID().length());
        assertTrue(m.getMessageID().matches("\\d{10}"));
        assertTrue(m.checkMessageID());
        assertEquals("Message ID generated: " + m.getMessageID(), m.messageIDStatus());
    }

    @Test
    void testMessageIDTooLongFails() {
        Message m = new Message("12345678901", 0, "+27718693002", MSG1);
        assertFalse(m.checkMessageID());
    }

    // Send / disregard / store
    @Test
    void testSentMessageSend() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    void testSentMessageDisregard() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    void testSentMessageStore() throws IOException {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
        String json = Files.readString(tempDir.resolve("messages.json"));
        assertTrue(json.contains("00:0:HITONIGHT"));
        assertTrue(json.contains(MSG1));
    }

    @Test
    void testStoringTwoMessagesKeepsBoth() throws IOException {
        message1.sentMessage(3);
        message2.sentMessage(3);
        String json = Files.readString(tempDir.resolve("messages.json"));
        assertTrue(json.contains("00:0:HITONIGHT"));
        assertTrue(json.contains("01:1:HIPAYMENT"));
    }

    // Totals and printing
    @Test
    void testTotalMessagesSent() {
        message1.sentMessage(1);   // sent
        message2.sentMessage(2);   // discarded
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    void testPrintMessagesShowsSentMessagesInOrder() {
        message1.sentMessage(1);
        String output = Message.printMessages();
        int id = output.indexOf("Message ID: 0012345678");
        int hash = output.indexOf("Message Hash: 00:0:HITONIGHT");
        int rec = output.indexOf("Recipient: +27718693002");
        int msg = output.indexOf("Message: " + MSG1);
        assertTrue(id >= 0 && id < hash && hash < rec && rec < msg);
    }
}
