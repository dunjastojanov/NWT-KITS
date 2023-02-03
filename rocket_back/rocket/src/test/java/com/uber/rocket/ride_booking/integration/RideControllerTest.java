package com.uber.rocket.ride_booking.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uber.rocket.configuration.TestConfig;
import com.uber.rocket.dto.ChangeStatusDTO;
import com.uber.rocket.dto.DestinationDTO;
import com.uber.rocket.dto.RideDTO;
import com.uber.rocket.dto.VehicleDTO;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.ride.UserRidingStatus;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleStatus;
import com.uber.rocket.entity.user.VehicleType;
import com.uber.rocket.repository.RideRepository;
import com.uber.rocket.repository.VehicleRepository;
import com.uber.rocket.ride_booking.utils.ride.RideCreationService;
import com.uber.rocket.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.uber.rocket.ride_booking.utils.destination.DestinationCreation.getDestinationDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
@TestConfiguration("classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class RideControllerTest {
    private static final String URL_PREFIX = "/api/ride";
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    private NotificationService notificationService;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Creating ride where ride id is negative")
    void createRideTest1() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithNegativeId();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is invalid because of id")
    void createRideTest2() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithInvalidUserDTOId();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is invalid because of blank first name")
    void createRideTest3() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithInvalidUserDTOFirstName();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is invalid because of blank last name")
    void createRideTest4() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithInvalidUserDTOLastName();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is invalid because of blank email")
    void createRideTest5() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithInvalidUserDTOBlankEmail();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is invalid because of email format")
    void createRideTest6() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithInvalidUserDTOEmailFormat();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is invalid because of role")
    void createRideTest7() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithInvalidUserDTORole();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is valid but rideDTO list of statusUserDto is null")
    void createRideTest8() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithInvalidStatusUserDto();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is valid, no riding pals, estimated Distances is negative ")
    void createRideTest9() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithNegativeEstimatedDistance();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is valid, no riding pals, estimated time is negative ")
    void createRideTest10() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithNegativeEstimatedTime();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is valid, no riding pals, price is negative ")
    void createRideTest11() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithNegativePrice();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is valid, no riding pals, route is blank ")
    void createRideTest12() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWitBlankRoute();
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @DisplayName("Creating ride where userDTO is valid, no riding pals,destinations are invalid")
    @NullAndEmptySource
    @MethodSource(value = "getFalseDestinationLists")
    void createRideTest13(List<DestinationDTO> destinations) throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithValidRoute();
        rideDTO.setDestinations(destinations);
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    public static List getFalseDestinationLists() {
        DestinationDTO destinationDTO1 = new DestinationDTO();
        List<DestinationDTO> list1 = new ArrayList<>();
        list1.add(destinationDTO1);
        List<DestinationDTO> list2 = new ArrayList<>();
        DestinationDTO destinationDTO2 = new DestinationDTO();
        destinationDTO2.setAddress("Brace Krkljus 12 Novi Sad");
        list2.add(destinationDTO2);
        List<DestinationDTO> list3 = new ArrayList<>();
        DestinationDTO destinationDTO3 = new DestinationDTO();
        destinationDTO3.setAddress("Brace Krkljus 12 Novi Sad");
        destinationDTO3.setLongitude(99.1);
        list3.add(destinationDTO3);
        List<DestinationDTO> list4 = new ArrayList<>();
        DestinationDTO destinationDTO4 = new DestinationDTO();
        destinationDTO4.setAddress("Brace Krkljus 12 Novi Sad");
        destinationDTO4.setLongitude(99.1);
        destinationDTO4.setLatitude(99.1);
        list4.add(destinationDTO4);
        List<DestinationDTO> list5 = new ArrayList<>();
        DestinationDTO destinationDTO5 = new DestinationDTO();
        destinationDTO5.setAddress("Brace Krkljus 12 Novi Sad");
        destinationDTO5.setLongitude(99.1);
        destinationDTO5.setLatitude(99.1);
        destinationDTO5.setIndex(1);
        list5.add(destinationDTO5);
        return List.of(list1, list2, list3, list4, list5).stream().toList();
    }

    public static List<DestinationDTO> getValidDestinations() {
        DestinationDTO destinationDTO = getDestinationDTO();
        List<DestinationDTO> destinationDTOS = new ArrayList<>();
        destinationDTOS.add(destinationDTO);
        destinationDTOS.add(destinationDTO);
        return destinationDTOS;
    }

    public static List getInvalidVehicles() {
        VehicleDTO vehicleDTO1 = new VehicleDTO();
        VehicleDTO vehicleDTO2 = new VehicleDTO();
        vehicleDTO2.setType(VehicleType.COUPE);
        VehicleDTO vehicleDTO3 = new VehicleDTO();
        vehicleDTO3.setType(VehicleType.CARAVAN);
        VehicleDTO vehicleDTO4 = new VehicleDTO();
        vehicleDTO4.setType(VehicleType.CONVERTIBLE);
        VehicleDTO vehicleDTO5 = new VehicleDTO();
        vehicleDTO5.setType(VehicleType.HATCHBACK);
        VehicleDTO vehicleDTO6 = new VehicleDTO();
        vehicleDTO6.setType(VehicleType.LIMOUSINE);
        VehicleDTO vehicleDTO7 = new VehicleDTO();
        vehicleDTO7.setType(VehicleType.MINIVAN);
        VehicleDTO vehicleDTO8 = new VehicleDTO();
        vehicleDTO8.setType(VehicleType.SUV);
        VehicleDTO vehicleDTO9 = new VehicleDTO();
        vehicleDTO9.setType(VehicleType.COUPE);
        vehicleDTO9.setLatitude((double) -1);
        VehicleDTO vehicleDTO10 = new VehicleDTO();
        vehicleDTO10.setLatitude(100.5);
        vehicleDTO10.setLongitude(-15.5);
        vehicleDTO10.setType(VehicleType.CARAVAN);
        VehicleDTO vehicleDTO11 = new VehicleDTO();
        vehicleDTO11.setLatitude(100.5);
        vehicleDTO11.setLongitude(15.5);
        vehicleDTO11.setType(VehicleType.CARAVAN);
        VehicleDTO vehicleDTO12 = new VehicleDTO();
        vehicleDTO12.setLatitude(100.5);
        vehicleDTO12.setLongitude(15.5);
        vehicleDTO12.setType(VehicleType.CARAVAN);
        vehicleDTO12.setPetFriendly(false);
        return List.of(vehicleDTO1, vehicleDTO2, vehicleDTO3, vehicleDTO4, vehicleDTO5, vehicleDTO6, vehicleDTO7, vehicleDTO8, vehicleDTO9, vehicleDTO10, vehicleDTO11, vehicleDTO12);
    }

    @ParameterizedTest
    @DisplayName("Creating ride where userDTO is valid, no riding pals,destinations is valid, vehicleDTO invalid")
    @MethodSource(value = "getInvalidVehicles")
    void createRideTest14(VehicleDTO vehicleDTO) throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithValidRoute();
        rideDTO.setDestinations(getValidDestinations());
        rideDTO.setVehicle(vehicleDTO);
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    public VehicleDTO getGoodVehicleDTO() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setPetFriendly(false);
        vehicleDTO.setKidFriendly(false);
        vehicleDTO.setType(VehicleType.SUV);
        vehicleDTO.setLatitude(10.1);
        vehicleDTO.setLongitude(10.1);
        return vehicleDTO;
    }

    @Test
    @DisplayName("Creating ride where userDTO is valid, no riding pals,destinations is valid, vehicleDTO is valid")
    void createRideTest15() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithValidRoute();
        rideDTO.setDestinations(getValidDestinations());
        rideDTO.setVehicle(getGoodVehicleDTO());
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is valid, no riding pals,destinations is valid, vehicleDTO is valid, time is blank")
    void createRideTest16() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithValidRoute();
        rideDTO.setDestinations(getValidDestinations());
        rideDTO.setVehicle(getGoodVehicleDTO());
        rideDTO.setIsNow(false);
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Creating ride where userDTO is valid, no riding pals,destinations is valid, vehicleDTO is valid, time is valid, features are null")
    void createRideTest17() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithValidRoute();
        rideDTO.setDestinations(getValidDestinations());
        rideDTO.setVehicle(getGoodVehicleDTO());
        rideDTO.setIsNow(false);
        rideDTO.setTime("1256.1");
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    public static List getRideStatuses() {
        return List.of(RideStatus.REQUESTED, RideStatus.ENDED, RideStatus.CONFIRMED, RideStatus.SCHEDULED);
    }

    @ParameterizedTest
    @MethodSource(value = "getRideStatuses")
    @DisplayName("Creating ride where userDTO is valid, no riding pals,destinations is valid, vehicleDTO is valid, time is valid, features are empty array, time is now")
    void createRideTest18(RideStatus rideStatus) throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createRideDTOWithValidRoute();
        rideDTO.setDestinations(getValidDestinations());
        rideDTO.setVehicle(getGoodVehicleDTO());
        rideDTO.setIsNow(false);
        rideDTO.setTime("1256.1");
        rideDTO.setFeatures(new ArrayList<>());
        rideDTO.setRideStatus(rideStatus);
        rideDTO.setIsNow(true);
        mockMvc.perform(post(URL_PREFIX + "/currentRide", rideDTO))
                .andExpect(status().isBadRequest());
    }

    //TODO prepravi
    @Test
    @DisplayName("Positive test for creating ride")
    void createRideTest19() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        RideDTO rideDTO = RideCreationService.createValidRideDto();
        rideDTO.setDestinations(getValidDestinations());
        rideDTO.setIsRouteFavorite(false);
        rideDTO.setRideStatus(RideStatus.REQUESTED);
        String dto = new ObjectMapper().writeValueAsString(rideDTO);
        mockMvc.perform(post(URL_PREFIX + "/currentRide").contentType(MediaType.APPLICATION_JSON).content(dto))
                .andExpect(status().isOk());
    }

    @Test
    void changeRideStatusNoRideWithPassedId() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/currentRideId/10"))
                .andExpect(status().isOk());
    }

    @Test
    void changeRideStatusPositiveTest() throws Exception {
        mockMvc.perform(get(URL_PREFIX + "/currentRideId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideId").value(1));
    }

    @Test
    @DisplayName("Fail to notify driver, no ride with id 10")
    void createPalsNotifsAndLookForDriverTest1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL_PREFIX + "/post-create-ride/10"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(Boolean.parseBoolean(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Passengers didn't accept ride")
    void createPalsNotifsAndLookForDriverTest4() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL_PREFIX + "/post-create-ride/7"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(Boolean.parseBoolean(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Passengers did accepted ride, but no driver")
    void createPalsNotifsAndLookForDriverTest2() throws Exception {
        Optional<Vehicle> vehicle = vehicleRepository.findById(1L);
        vehicle.get().setStatus(VehicleStatus.INACTIVE);
        vehicleRepository.save(vehicle.get());
        MvcResult mvcResult = mockMvc.perform(get(URL_PREFIX + "/post-create-ride/6"))
                .andExpect(status().isOk()).andReturn();
        Optional<Ride> ride = rideRepository.findById(6L);
        assertEquals(RideStatus.DENIED, ride.get().getStatus());
        assertFalse(Boolean.parseBoolean(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Passengers did accept ride, available driver")
    void createPalsNotifsAndLookForDriverTest3() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL_PREFIX + "/post-create-ride/8"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(Boolean.parseBoolean(mvcResult.getResponse().getContentAsString()));
        Optional<Ride> ride = rideRepository.findById(8L);
        for (int i = 1; i < ride.get().getPassengers().size(); i++) {
            int size = notificationService.getNotificationsForUser(ride.get().getPassengers().stream().toList().get(i).getUser()).size();
            assertEquals(1, size);
        }
    }

    @Test
    void gettingPassengerPalWithWrongEmail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL_PREFIX + "/pal/bre@gmail.com"))
                .andExpect(status().isNotFound()).andReturn();
        assertEquals("User not found", mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("User has current ride")
    void getRidingPalTest2() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL_PREFIX + "/pal/marica@gmail.com"))
                .andExpect(status().isForbidden()).andReturn();
        assertEquals("User already has scheduled ride.", mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("User doesn't have current ride")
    void getRidingPal() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(URL_PREFIX + "/pal/jovica@gmail.com"))
                .andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("jovica@gmail.com"));
    }


}