package com.poe.part1;

import java.util.Scanner;

//made this pure console application that demonstrates registration and login.
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("PART 1: Registration & Login\n");

        //Registration
        System.out.println("Register a new account");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter username (must contain _ and be ≤ 5 characters): ");
        String username = scanner.nextLine();

        System.out.print("Enter password (min 8 chars, 1 capital, 1 number, 1 special): ");
        String password = scanner.nextLine();

        System.out.print("Enter South African cell number (e.g. +27838968976): ");
        String cell = scanner.nextLine();

        // Create the user object
        login = new Login(firstName, lastName, username, password, cell);

        // Show registration result
        System.out.println("\nRegistration Result");
        System.out.println(login.registerUser());

        // Login
        System.out.println("\nLogin");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        System.out.println("\nLogin Status");
        System.out.println(login.returnLoginStatus(loginUsername, loginPassword));

        scanner.close();
    }
}