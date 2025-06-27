/**
 *  Project: Airport-CLI
 *  Author: NickPow SD12
 *  Date: June 27, 2025
 */

package com.example.airportcli;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Airport CLI ===");

        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. List airports in a city");
            System.out.println("2. List aircraft flown by a passenger");
            System.out.println("3. List airports for an aircraft");
            System.out.println("4. List airports used by a passenger");
            System.out.println("5. Exit");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // TODO: GET request to API for city airports
                    break;
                case "2":
                    // TODO: GET request for passenger's aircraft
                    break;
                case "3":
                    // TODO: GET request for aircraft's airports
                    break;
                case "4":
                    // TODO: GET request for airports used by passenger
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }

        System.out.println("Goodbye.");
        scanner.close();
    }
}
