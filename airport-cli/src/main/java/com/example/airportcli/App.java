package com.example.airportcli;

import java.net.http.*;
import java.net.URI;
import java.util.Scanner;
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

    public static void viewAirportsInCity() {
        System.out.println("\n--- Airports in City ---");
        listCities();

        long cityId = promptForId("Enter City ID: ", API_URL + "/cities");

        if (cityId == -1) return;

        String url = API_URL + "/cities/" + cityId + "/airports";
        String response = sendGetRequest(url);

        if (response.isEmpty()) {
            System.out.println("No airports found or error occurred.");
            return;
        }

        JSONArray airports = new JSONArray(response);

        for (int i = 0; i < airports.length(); i++) {
            JSONObject airport = airports.getJSONObject(i);
            System.out.printf("Airport ID: %d | Name: %s | Code: %s%n",
                    airport.getLong("id"), airport.getString("name"), airport.getString("code"));
        }
    }

    public static void viewAircraftForPassenger() {
        System.out.println("\n--- Aircraft Flown by Passenger ---");
        listPassengers();

        long passengerId = promptForId("Enter Passenger ID: ", API_URL + "/passengers");

        if (passengerId == -1) return;

        String url = API_URL + "/passengers/" + passengerId + "/aircraft";
        String response = sendGetRequest(url);

        if (response.isEmpty()) {
            System.out.println("No aircraft found or error occurred.");
            return;
        }

        JSONArray aircraftList = new JSONArray(response);

        for (int i = 0; i < aircraftList.length(); i++) {
            JSONObject aircraft = aircraftList.getJSONObject(i);
            System.out.printf("Aircraft ID: %d | Type: %s | Airline: %s | Capacity: %d%n",
                    aircraft.getLong("id"), aircraft.getString("type"),
                    aircraft.getString("airlineName"), aircraft.getInt("numberOfPassengers"));
        }
    }

    public static void viewAirportsForAircraft() {
        System.out.println("\n--- Airports Used by Aircraft ---");
        listAircraft();

        long aircraftId = promptForId("Enter Aircraft ID: ", API_URL + "/aircraft");

        if (aircraftId == -1) return;

        String url = API_URL + "/aircraft/" + aircraftId + "/airports";
        String response = sendGetRequest(url);

        if (response.isEmpty()) {
            System.out.println("No airports found or error occurred.");
            return;
        }

        JSONArray airports = new JSONArray(response);

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

        if (response.isEmpty()) {
            System.out.println("No airports found or error occurred.");
            return;
        }

        JSONArray airports = new JSONArray(response);

        for (int i = 0; i < airports.length(); i++) {
            JSONObject airport = airports.getJSONObject(i);
            System.out.printf("Airport ID: %d | Name: %s | Code: %s%n",
                    airport.getLong("id"), airport.getString("name"), airport.getString("code"));
        }
    }

    public static long promptForId(String message, String listEndpoint) {
    Scanner scanner = new Scanner(System.in);  // Re-initialize scanner inside the method

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


    public static String sendGetRequest(String url) {
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

    public static void listCities() {
        String response = sendGetRequest(API_URL + "/cities");

        if (response.isEmpty()) {
            System.out.println("No cities found or error occurred.");
            return;
        }

        JSONArray cities = new JSONArray(response);

        for (int i = 0; i < cities.length(); i++) {
            JSONObject city = cities.getJSONObject(i);
            System.out.printf("[%d] %s (%s) - Population: %d%n",
                    city.getLong("id"), city.getString("name"),
                    city.getString("province"), city.getInt("population"));
        }
    }

    public static void listPassengers() {
        String response = sendGetRequest(API_URL + "/passengers");

        if (response.isEmpty()) {
            System.out.println("No passengers found or error occurred.");
            return;
        }

        JSONArray passengers = new JSONArray(response);

        for (int i = 0; i < passengers.length(); i++) {
            JSONObject passenger = passengers.getJSONObject(i);
            System.out.printf("[%d] %s %s - Phone: %s%n",
                    passenger.getLong("id"), passenger.getString("firstName"),
                    passenger.getString("lastName"), passenger.getString("phoneNumber"));
        }
    }

    public static void listAircraft() {
        String response = sendGetRequest(API_URL + "/aircraft");

        if (response.isEmpty()) {
            System.out.println("No aircraft found or error occurred.");
            return;
        }

        JSONArray aircraftList = new JSONArray(response);

        for (int i = 0; i < aircraftList.length(); i++) {
            JSONObject aircraft = aircraftList.getJSONObject(i);
            System.out.printf("[%d] %s - Airline: %s | Capacity: %d%n",
                    aircraft.getLong("id"), aircraft.getString("type"),
                    aircraft.getString("airlineName"), aircraft.getInt("numberOfPassengers"));
        }
    }
}
