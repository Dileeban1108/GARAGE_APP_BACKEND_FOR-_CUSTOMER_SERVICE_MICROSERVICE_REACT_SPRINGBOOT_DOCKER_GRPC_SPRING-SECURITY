package com.isa.customer_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isa.customer_service.enums.ServiceStatus;
import com.isa.customer_service.model.Bookings;
import com.isa.customer_service.model.UserHistory;
import com.isa.customer_service.model.Vehicles;
import com.isa.customer_service.service.BookingService;
import com.isa.customer_service.service.GarageProvideServiceImpl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {BookingController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class BookingControllerDiffblueTest {
    @Autowired
    private BookingController bookingController;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private GarageProvideServiceImpl garageProvideServiceImpl;

    /**
     * Test {@link BookingController#BookingController(GarageProvideServiceImpl)}.
     * <p>
     * Method under test:
     * {@link BookingController#BookingController(GarageProvideServiceImpl)}
     */
    @Test
    @DisplayName("Test BookingController(GarageProvideServiceImpl)")
    void testBookingController() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     BookingController.bookingService
        //     BookingController.garageProvideService

        // Arrange
        BookingController bookingController = new BookingController();

        // Act
        bookingController.BookingController(new GarageProvideServiceImpl(null));
    }

    /**
     * Test {@link BookingController#createBooking(Bookings)}.
     * <ul>
     *   <li>Then status four hundred.</li>
     * </ul>
     * <p>
     * Method under test: {@link BookingController#createBooking(Bookings)}
     */
    @Test
    @DisplayName("Test createBooking(Bookings); then status four hundred")
    void testCreateBooking_thenStatusFourHundred() throws Exception {
        // Arrange
        when(bookingService.createBooking(Mockito.<Bookings>any())).thenThrow(new IllegalStateException("foo"));

        Bookings bookings = new Bookings();
        bookings.setBookingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        bookings.setBookingId(1L);
        bookings.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        bookings.setRejectionMessage("Rejection Message");
        bookings.setServiceAcceptStatus(ServiceStatus.PENDING);
        bookings.setServiceId(1L);
        bookings.setServiceName("Service Name");
        bookings.setSlotId(1);
        bookings.setUserId(1L);
        bookings.setVehicleNo("Vehicle No");
        String content = (new ObjectMapper()).writeValueAsString(bookings);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/bookings/createBooking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Test {@link BookingController#createBooking(Bookings)}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BookingController#createBooking(Bookings)}
     */
    @Test
    @DisplayName("Test createBooking(Bookings); then status isOk()")
    void testCreateBooking_thenStatusIsOk() throws Exception {
        // Arrange
        Bookings bookings = new Bookings();
        bookings.setBookingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        bookings.setBookingId(1L);
        bookings.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        bookings.setRejectionMessage("Rejection Message");
        bookings.setServiceAcceptStatus(ServiceStatus.PENDING);
        bookings.setServiceId(1L);
        bookings.setServiceName("Service Name");
        bookings.setSlotId(1);
        bookings.setUserId(1L);
        bookings.setVehicleNo("Vehicle No");
        when(bookingService.createBooking(Mockito.<Bookings>any())).thenReturn(bookings);

        Bookings bookings2 = new Bookings();
        bookings2.setBookingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        bookings2.setBookingId(1L);
        bookings2.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        bookings2.setRejectionMessage("Rejection Message");
        bookings2.setServiceAcceptStatus(ServiceStatus.PENDING);
        bookings2.setServiceId(1L);
        bookings2.setServiceName("Service Name");
        bookings2.setSlotId(1);
        bookings2.setUserId(1L);
        bookings2.setVehicleNo("Vehicle No");
        String content = (new ObjectMapper()).writeValueAsString(bookings2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/bookings/createBooking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"bookingId\":1,\"userId\":1,\"slotId\":1,\"serviceId\":1,\"serviceName\":\"Service Name\",\"serviceAcceptStatus"
                                        + "\":\"PENDING\",\"createdAt\":0,\"bookingDate\":0,\"rejectionMessage\":\"Rejection Message\",\"vehicleNo\":\"Vehicle"
                                        + " No\"}"));
    }

    /**
     * Test {@link BookingController#addVehicle(Vehicles)}.
     * <p>
     * Method under test: {@link BookingController#addVehicle(Vehicles)}
     */
    @Test
    @DisplayName("Test addVehicle(Vehicles)")
    void testAddVehicle() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        BookingController bookingController = new BookingController();

        Vehicles vehicles = new Vehicles();
        vehicles.setFuelType("Fuel Type");
        vehicles.setUserId(1L);
        vehicles.setVehicleBrand("Vehicle Brand");
        vehicles.setVehicleModel("Vehicle Model");
        vehicles.setVehicleNo("Vehicle No");
        vehicles.setVehicleType("Vehicle Type");

        // Act
        ResponseEntity<Vehicles> actualAddVehicleResult = bookingController.addVehicle(vehicles);

        // Assert
        HttpStatusCode statusCode = actualAddVehicleResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertNull(actualAddVehicleResult.getBody());
        assertEquals(500, actualAddVehicleResult.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
        assertFalse(actualAddVehicleResult.hasBody());
        assertTrue(actualAddVehicleResult.getHeaders().isEmpty());
    }

    /**
     * Test {@link BookingController#getAllBookings()}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BookingController#getAllBookings()}
     */
    @Test
    @DisplayName("Test getAllBookings(); then status isNotFound()")
    void testGetAllBookings_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(bookingService.getAllBookings()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/getAllBookings");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link BookingController#getAllBookings()}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BookingController#getAllBookings()}
     */
    @Test
    @DisplayName("Test getAllBookings(); then status isOk()")
    void testGetAllBookings_thenStatusIsOk() throws Exception {
        // Arrange
        UserHistory userHistory = new UserHistory();
        userHistory.setBookingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userHistory.setBookingId(1L);
        userHistory.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userHistory.setFuelType("Fuel Type");
        userHistory.setRejectionMessage("Rejection Message");
        userHistory.setServiceAcceptStatus(ServiceStatus.PENDING);
        userHistory.setServiceId(1L);
        userHistory.setServiceName("Service Name");
        userHistory.setSlotId(1);
        userHistory.setUserId(1L);
        userHistory.setVehicleBrand("Vehicle Brand");
        userHistory.setVehicleModel("Vehicle Model");
        userHistory.setVehicleNo("Vehicle No");
        userHistory.setVehicleType("Vehicle Type");

        ArrayList<UserHistory> userHistoryList = new ArrayList<>();
        userHistoryList.add(userHistory);
        when(bookingService.getAllBookings()).thenReturn(userHistoryList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/getAllBookings");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"bookingId\":1,\"userId\":1,\"slotId\":1,\"serviceId\":1,\"serviceName\":\"Service Name\",\"serviceAcceptStatus"
                                        + "\":\"PENDING\",\"createdAt\":0,\"bookingDate\":0,\"rejectionMessage\":\"Rejection Message\",\"vehicleNo\":\"Vehicle"
                                        + " No\",\"vehicleType\":\"Vehicle Type\",\"vehicleBrand\":\"Vehicle Brand\",\"vehicleModel\":\"Vehicle Model\","
                                        + "\"fuelType\":\"Fuel Type\"}]"));
    }

    /**
     * Test {@link BookingController#getVehiclesByUserId()}.
     * <ul>
     *   <li>Given {@link Vehicles} (default constructor) FuelType is
     * {@code Fuel Type}.</li>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BookingController#getVehiclesByUserId()}
     */
    @Test
    @DisplayName("Test getVehiclesByUserId(); given Vehicles (default constructor) FuelType is 'Fuel Type'; then status isOk()")
    void testGetVehiclesByUserId_givenVehiclesFuelTypeIsFuelType_thenStatusIsOk() throws Exception {
        // Arrange
        Vehicles vehicles = new Vehicles();
        vehicles.setFuelType("Fuel Type");
        vehicles.setUserId(1L);
        vehicles.setVehicleBrand("Vehicle Brand");
        vehicles.setVehicleModel("Vehicle Model");
        vehicles.setVehicleNo("Vehicle No");
        vehicles.setVehicleType("Vehicle Type");

        ArrayList<Vehicles> vehiclesList = new ArrayList<>();
        vehiclesList.add(vehicles);
        when(bookingService.getVehicles()).thenReturn(vehiclesList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/getVehicles");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"vehicleNo\":\"Vehicle No\",\"userId\":1,\"vehicleType\":\"Vehicle Type\",\"vehicleBrand\":\"Vehicle Brand\","
                                        + "\"vehicleModel\":\"Vehicle Model\",\"fuelType\":\"Fuel Type\"}]"));
    }

    /**
     * Test {@link BookingController#getVehiclesByUserId()}.
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BookingController#getVehiclesByUserId()}
     */
    @Test
    @DisplayName("Test getVehiclesByUserId(); then status isNotFound()")
    void testGetVehiclesByUserId_thenStatusIsNotFound() throws Exception {
        // Arrange
        when(bookingService.getVehicles()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/getVehicles");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link BookingController#getBookingsByUserId()}.
     * <ul>
     *   <li>Then content string a string.</li>
     * </ul>
     * <p>
     * Method under test: {@link BookingController#getBookingsByUserId()}
     */
    @Test
    @DisplayName("Test getBookingsByUserId(); then content string a string")
    void testGetBookingsByUserId_thenContentStringAString() throws Exception {
        // Arrange
        UserHistory userHistory = new UserHistory();
        userHistory.setBookingDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userHistory.setBookingId(1L);
        userHistory.setCreatedAt(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        userHistory.setFuelType("Fuel Type");
        userHistory.setRejectionMessage("Rejection Message");
        userHistory.setServiceAcceptStatus(ServiceStatus.PENDING);
        userHistory.setServiceId(1L);
        userHistory.setServiceName("Service Name");
        userHistory.setSlotId(1);
        userHistory.setUserId(1L);
        userHistory.setVehicleBrand("Vehicle Brand");
        userHistory.setVehicleModel("Vehicle Model");
        userHistory.setVehicleNo("Vehicle No");
        userHistory.setVehicleType("Vehicle Type");

        ArrayList<UserHistory> userHistoryList = new ArrayList<>();
        userHistoryList.add(userHistory);
        when(bookingService.getBookings()).thenReturn(userHistoryList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/getBookings");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"bookingId\":1,\"userId\":1,\"slotId\":1,\"serviceId\":1,\"serviceName\":\"Service Name\",\"serviceAcceptStatus"
                                        + "\":\"PENDING\",\"createdAt\":0,\"bookingDate\":0,\"rejectionMessage\":\"Rejection Message\",\"vehicleNo\":\"Vehicle"
                                        + " No\",\"vehicleType\":\"Vehicle Type\",\"vehicleBrand\":\"Vehicle Brand\",\"vehicleModel\":\"Vehicle Model\","
                                        + "\"fuelType\":\"Fuel Type\"}]"));
    }

    /**
     * Test {@link BookingController#getBookingsByUserId()}.
     * <ul>
     *   <li>Then content string {@code []}.</li>
     * </ul>
     * <p>
     * Method under test: {@link BookingController#getBookingsByUserId()}
     */
    @Test
    @DisplayName("Test getBookingsByUserId(); then content string '[]'")
    void testGetBookingsByUserId_thenContentStringLeftSquareBracketRightSquareBracket() throws Exception {
        // Arrange
        when(bookingService.getBookings()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/getBookings");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link BookingController#deleteVehicle(String)}.
     * <p>
     * Method under test: {@link BookingController#deleteVehicle(String)}
     */
    @Test
    @DisplayName("Test deleteVehicle(String)")
    void testDeleteVehicle() throws Exception {
        // Arrange
        doNothing().when(bookingService).deleteVehicle(Mockito.<String>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/bookings/deleteVehicle/{vehicleNo}",
                "Vehicle No");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Vehicle removed successfully!"));
    }

    /**
     * Test {@link BookingController#deleteBooking(Long)}.
     * <p>
     * Method under test: {@link BookingController#deleteBooking(Long)}
     */
    @Test
    @DisplayName("Test deleteBooking(Long)")
    void testDeleteBooking() throws Exception {
        // Arrange
        doNothing().when(bookingService).deleteBooking(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/bookings/deleteBooking/{bookingId}",
                1L);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Booking removed successfully!"));
    }

    /**
     * Test {@link BookingController#getGarageServices()}.
     * <p>
     * Method under test: {@link BookingController#getGarageServices()}
     */
    @Test
    @DisplayName("Test getGarageServices()")
    void testGetGarageServices() throws Exception {
        // Arrange
        when(garageProvideServiceImpl.getGarageServices()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/bookings/services");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
