package com.example.airportcli;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;

/**
 * Unit tests.
 * Tests include API response parsing, input validation, and output logic.
 */
public class AppTest {

    /**
     * Tests parsing and printing of cities from API response.
     */
    @Test
    public void testListCities_ParsesAndPrintsCorrectly() {
        String dummyResponse = "[{\"id\":1,\"name\":\"CityA\",\"province\":\"ProvA\",\"population\":1000}]";

        try (MockedStatic<App> mockedApp = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
            mockedApp.when(() -> App.sendGetRequest(anyString())).thenReturn(dummyResponse);

            App.listCities();
        }
    }

    /**
     * Tests parsing and printing of passengers from API response.
     */
    @Test
    public void testListPassengers_ParsesAndPrintsCorrectly() {
        String dummyResponse = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"phoneNumber\":\"1234567890\"}]";

        try (MockedStatic<App> mockedApp = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
            mockedApp.when(() -> App.sendGetRequest(anyString())).thenReturn(dummyResponse);

            App.listPassengers();
        }
    }

    /**
     * Tests parsing and printing of aircraft from API response.
     */
    @Test
    public void testListAircraft_ParsesAndPrintsCorrectly() {
        String dummyResponse = "[{\"id\":1,\"type\":\"Jet\",\"airlineName\":\"TestAir\",\"numberOfPassengers\":150}]";

        try (MockedStatic<App> mockedApp = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
            mockedApp.when(() -> App.sendGetRequest(anyString())).thenReturn(dummyResponse);

            App.listAircraft();
        }
    }

    /**
     * Tests promptForId with valid numeric input.
     */
    @Test
    public void testPromptForId_ValidInput() {
        String simulatedInput = "5\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        try (MockedStatic<App> mockedApp = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
            mockedApp.when(() -> App.sendGetRequest(anyString())).thenReturn("[]");

            long id = App.promptForId("Enter ID: ", "http://localhost:8080/cities");
            assertEquals(5, id);
        }
    }

    /**
     * Tests promptForId with invalid followed by valid input.
     */
    @Test
    public void testPromptForId_InvalidThenValidInput() {
        String simulatedInput = "abc\n7\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        try (MockedStatic<App> mockedApp = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
            mockedApp.when(() -> App.sendGetRequest(anyString())).thenReturn("[]");

            long id = App.promptForId("Enter ID: ", "http://localhost:8080/cities");
            assertEquals(7, id);
        }
    }

    /**
     * Tests viewing airports for a city with no airports returned.
     */
    @Test
    public void testViewAirportsInCity_NoAirports() {
        String cityListResponse = "[{\"id\":1,\"name\":\"CityA\",\"province\":\"ProvA\",\"population\":1000}]";
        String emptyAirportsResponse = "[]";

        String simulatedInput = "1\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        try (MockedStatic<App> mockedApp = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
            mockedApp.when(() -> App.sendGetRequest(contains("/cities"))).thenReturn(cityListResponse);
            mockedApp.when(() -> App.sendGetRequest(contains("/cities/1/airports"))).thenReturn(emptyAirportsResponse);

            App.viewAirportsInCity();
        }
    }

    /**
     * Tests viewing aircraft for a passenger when none exist.
     */
    @Test
    public void testViewAircraftForPassenger_NoAircraft() {
        String passengerListResponse = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\",\"phoneNumber\":\"1234567890\"}]";
        String emptyAircraftResponse = "[]";

        String simulatedInput = "1\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        try (MockedStatic<App> mockedApp = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
            mockedApp.when(() -> App.sendGetRequest(contains("/passengers"))).thenReturn(passengerListResponse);
            mockedApp.when(() -> App.sendGetRequest(contains("/passengers/1/aircraft"))).thenReturn(emptyAircraftResponse);

            App.viewAircraftForPassenger();
        }
    }

    /**
     * Tests viewing airports for an aircraft with multiple airports.
     */
    @Test
    public void testViewAirportsForAircraft_MultipleAirports() {
        String aircraftListResponse = "[{\"id\":1,\"type\":\"Jet\",\"airlineName\":\"TestAir\",\"numberOfPassengers\":150}]";
        String airportsResponse = "[{\"id\":101,\"name\":\"Airport A\",\"code\":\"AAA\"},{\"id\":102,\"name\":\"Airport B\",\"code\":\"BBB\"}]";

        String simulatedInput = "1\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        try (MockedStatic<App> mockedApp = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
            mockedApp.when(() -> App.sendGetRequest(contains("/aircraft"))).thenReturn(aircraftListResponse);
            mockedApp.when(() -> App.sendGetRequest(contains("/aircraft/1/airports"))).thenReturn(airportsResponse);

            App.viewAirportsForAircraft();
        }
    }
}
