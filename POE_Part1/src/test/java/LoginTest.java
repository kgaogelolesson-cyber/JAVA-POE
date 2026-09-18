package com.poe.part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests that use the exact test data supplied in the assignment.
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        // A correctly registered user for login tests
        login = new Login("John", "Doe", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    // Username tests
    @Test
    void testUsernameCorrectlyFormatted() {
        login.setUsername("kyl_1");
        assertTrue(login.checkUserName());
    }

    @Test
    void testUsernameIncorrectlyFormatted() {
        login.setUsername("kyle!!!!!!!");
        assertFalse(login.checkUserName());
    }

    //  Password tests 
    @Test
    void testPasswordMeetsComplexity() {
        login.setPassword("Ch&&sec@ke99!");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    void testPasswordDoesNotMeetComplexity() {
        login.setPassword("password");
        assertFalse(login.checkPasswordComplexity());
    }

    // Cell phone tests 
    @Test
    void testCellPhoneCorrectlyFormatted() {
        login.setCellPhoneNumber("+27838968976");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    void testCellPhoneIncorrectlyFormatted() {
        login.setCellPhoneNumber("08966553");
        assertFalse(login.checkCellPhoneNumber());
    }

    // Login success / failure 
    @Test
    void testLoginSuccessful() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testLoginFailed() {
        assertFalse(login.loginUser("wrong", "wrong"));
    }

    // Message tests (assertEquals) 
    @Test
    void testRegisterUserMessages_UsernameCorrect() {
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhoneNumber("+27838968976");

        String result = login.registerUser();
        assertTrue(result.contains("Username successfully captured."));
    }

    @Test
    void testRegisterUserMessages_UsernameIncorrect() {
        login.setUsername("kyle!!!!!!!");
        String result = login.registerUser();
        assertTrue(result.contains("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length."));
    }

    @Test
    void testRegisterUserMessages_PasswordCorrect() {
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhoneNumber("+27838968976");

        String result = login.registerUser();
        assertTrue(result.contains("Password successfully captured."));
    }

    @Test
    void testRegisterUserMessages_PasswordIncorrect() {
        login.setPassword("password");
        String result = login.registerUser();
        assertTrue(result.contains("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character."));
    }

    @Test
    void testRegisterUserMessages_CellCorrect() {
        login.setCellPhoneNumber("+27838968976");
        String result = login.registerUser();
        assertTrue(result.contains("Cell phone number successfully added."));
    }

    @Test
    void testRegisterUserMessages_CellIncorrect() {
        login.setCellPhoneNumber("08966553");
        String result = login.registerUser();
        assertTrue(result.contains("Cell phone number incorrectly formatted or does not contain international code."));
    }

    @Test
    void testReturnLoginStatus_Success() {
        String expected = "Welcome John, Doe It is great to see you again.";
        assertEquals(expected, login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testReturnLoginStatus_Failed() {
        String expected = "Username or password incorrect, please try again.";
        assertEquals(expected, login.returnLoginStatus("wrong", "wrong"));
    }
}