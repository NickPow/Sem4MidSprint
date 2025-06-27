package com.example.airportcli;

import java.net.http.*;
import java.net.URI;
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;
import org.json.JSONArray;
import org.json.JSONObject;

public class App {

    private static final String API_URL = "http://localhost:8080";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- Airport API CLI ---");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. View airports in each city");
            System.out.println("2. View aircraft flown by each passenger");
            System.out.println("3. View airports aircraft take off from and land at");
            System.out.println("4. View airports used by each passenger");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAirportsInCity();
                    break;
                case "2":
                    viewAircraftForPassenger();
                    break;
                case "3":
                    viewAirportsForAircraft();
                    break;
                case "4":
                    viewAirportsUsedByPassenger();
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAirportsInCity() {
        System.out.println("\n--- Airports in City ---");
        listCities();

        long cityId = promptForId("Enter City ID: ", API_URL + "/cities");

        if (cityId == -1) return;

        String url = API_URL + "/cities/" + cityId + "/airports";
        String response = sendGetRequest(url);

        JSONArray airports = new JSONArray(response);

        if (airports.isEmpty()) {
            System.out.println("No airports found for this city.");
            return;
        }

        for (int i = 0; i < airports.length(); i++) {
            JSONObject airport = airports.getJSONObject(i);
            System.out.printf("Airport ID: %d | Name: %s | Code: %s%n",
                    airport.getLong("id"), airport.getString("name"), airport.getString("code"));
        }
    }

    private static void viewAircraftForPassenger() {
        System.out.println("\n--- Aircraft Flown by Passenger ---");
        listPassengers();

        long passengerId = promptForId("Enter Passenger ID: ", API_URL + "/passengers");

        if (passengerId == -1) return;

        String url = API_URL + "/passengers/" + passengerId + "/aircraft";
        String response = sendGetRequest(url);

        JSONArray aircraftList = new JSONArray(response);

        if (aircraftList.isEmpty()) {
            System.out.println("No aircraft found for this passenger.");
            return;
        }

        for (int i = 0; i < aircraftList.length(); i++) {
            JSONObject aircraft = aircraftList.getJSONObject(i);
            System.out.printf("Aircraft ID: %d | Type: %s | Airline: %s | Capacity: %d%n",
                    aircraft.getLong("id"), aircraft.getString("type"),
                    aircraft.getString("airlineName"), aircraft.getInt("numberOfPassengers"));
        }
    }

    private static void viewAirportsForAircraft() {
        System.out.println("\n--- Airports Used by Aircraft ---");
        listAircraft();

        long aircraftId = promptForId("Enter Aircraft ID: ", API_URL + "/aircraft");

        if (aircraftId == -1) return;

        String url = API_URL + "/aircraft/" + aircraftId + "/airports";
        String response = sendGetRequest(url);

        JSONArray airports = new JSONArray(response);

        if (airports.isEmpty()) {
            System.out.println("No airports found for this aircraft.");
            return;
        }

        for (int i = 0; i < airports.length(); i++) {
            JSONObject airport = airports.getJSONObject(i);
            System.out.printf("Airport ID: %d | Name: %s | Code: %s%n",
                    airport.getLong("id"), airport.getString("name"), airport.getString("code"));
        }
    }

    private static void viewAirportsUsedByPassenger() {
        System.out.println("\n--- Airports Used by Passenger ---");
        listPassengers();

        long passengerId = promptForId("Enter Passenger ID: ", API_URL + "/passengers");

        if (passengerId == -1) return;

        String url = API_URL + "/passengers/" + passengerId + "/airports";
        String response = sendGetRequest(url);

        JSONArray airports = new JSONArray(response);

        if (airports.isEmpty()) {
            System.out.println("No airports found for this passenger.");
            return;
        }

        for (int i = 0; i < airports.length(); i++) {
            JSONObject airport = airports.getJSONObject(i);
            System.out.printf("Airport ID: %d | Name: %s | Code: %s%n",
                    airport.getLong("id"), airport.getString("name"), airport.getString("code"));
        }
    }

  private static long promptForId(String message, String listEndpoint) {
    while (true) {
        System.out.print(message);
        String input = scanner.nextLine();

        try {
            long id = Long.parseLong(input);

            
            return id;
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid numeric ID.");
        }
    }
}




    private static String sendGetRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return "";
            }
        } catch (Exception e) {
            System.out.println("Error connecting to API: " + e.getMessage());
            return "";
        }
    }

    private static void listCities() {
        String response = sendGetRequest(API_URL + "/cities");
        JSONArray cities = new JSONArray(response);

        for (int i = 0; i < cities.length(); i++) {
            JSONObject city = cities.getJSONObject(i);
            System.out.printf("[%d] %s (%s) - Population: %d%n",
                    city.getLong("id"), city.getString("name"),
                    city.getString("province"), city.getInt("population"));
        }
    }

    private static void listPassengers() {
        String response = sendGetRequest(API_URL + "/passengers");
        JSONArray passengers = new JSONArray(response);

        for (int i = 0; i < passengers.length(); i++) {
            JSONObject passenger = passengers.getJSONObject(i);
            System.out.printf("[%d] %s %s - Phone: %s%n",
                    passenger.getLong("id"), passenger.getString("firstName"),
                    passenger.getString("lastName"), passenger.getString("phoneNumber"));
        }
    }

    private static void listAircraft() {
        String response = sendGetRequest(API_URL + "/aircraft");
        JSONArray aircraftList = new JSONArray(response);

        for (int i = 0; i < aircraftList.length(); i++) {
            JSONObject aircraft = aircraftList.getJSONObject(i);
            System.out.printf("[%d] %s - Airline: %s | Capacity: %d%n",
                    aircraft.getLong("id"), aircraft.getString("type"),
                    aircraft.getString("airlineName"), aircraft.getInt("numberOfPassengers"));
        }
    }
}
