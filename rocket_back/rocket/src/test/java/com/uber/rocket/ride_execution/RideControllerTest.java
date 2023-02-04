package com.uber.rocket.ride_execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.uber.rocket.configuration.TestConfig;
import com.uber.rocket.dto.LocationDTO;
import com.uber.rocket.dto.RideSimulationDTO;
import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.ride.RideStatus;
import com.uber.rocket.entity.user.DriverReport;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleStatus;
import com.uber.rocket.repository.DriverReportRepository;
import com.uber.rocket.repository.RideRepository;
import com.uber.rocket.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
@TestConfiguration("classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
class RideControllerTest {
    private static final String RIDE_URL_PREFIX = "/api/ride";
    private static final String VEHICLE_URL_PREFIX = "/api/vehicle";
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    private DriverReportRepository driverReportRepository;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void startRideSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/change-status/4?status=STARTED"))
                .andExpect(status().isOk()).andReturn();
        Optional<Ride> ride = rideRepository.findById(4L);
        assertEquals(RideStatus.STARTED, ride.get().getStatus());
    }

    @Test
    void endRideSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/change-status/4?status=ENDED"))
                .andExpect(status().isOk()).andReturn();
        Optional<Ride> ride = rideRepository.findById(4L);
        assertEquals(RideStatus.ENDED, ride.get().getStatus());
        assertNotNull(ride.get().getEndTime());
    }

    @Test
    void rideDoesNotExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/change-status/99?status=STARTED"))
                .andExpect(status().isOk()).andReturn();
        assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void statusIsNotDefined() throws Exception {
        Optional<Ride> ride = rideRepository.findById(4L);
        RideStatus expectedStatus = ride.get().getStatus();
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/change-status/4"))
                .andExpect(status().isBadRequest()).andReturn();
        Optional<Ride> rideAfter = rideRepository.findById(4L);
        RideStatus actualStatus = rideAfter.get().getStatus();
        assertEquals(expectedStatus, actualStatus);
    }


    @Test
    void changeWrongStatus() throws Exception {
        Optional<Ride> ride = rideRepository.findById(4L);
        RideStatus expectedStatus = ride.get().getStatus();
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/change-status/4?status=DENIED"))
                .andExpect(status().isOk()).andReturn();
        Optional<Ride> rideAfter = rideRepository.findById(4L);
        RideStatus actualStatus = rideAfter.get().getStatus();
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void getRideSimulationActiveVehicleSuccessfully() throws Exception {
        Vehicle vehicle = vehicleRepository.findById(1L).get();
        vehicle.setStatus(VehicleStatus.ACTIVE);
        vehicleRepository.save(vehicle);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/simulation-ride/1"))
                .andExpect(status().isOk()).andReturn();
        Gson gson = new Gson();
        RideSimulationDTO rideSimulationDTO = gson.fromJson(mvcResult.getResponse().getContentAsString(), RideSimulationDTO.class);
        assertEquals(VehicleStatus.ACTIVE, rideSimulationDTO.getVehicleStatus());
    }

    @Test
    void getRideSimulationInactiveVehicleSuccessfully() throws Exception {
        Vehicle vehicle = vehicleRepository.findById(4L).get();
        vehicle.setStatus(VehicleStatus.INACTIVE);
        vehicleRepository.save(vehicle);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/simulation-ride/4"))
                .andExpect(status().isOk()).andReturn();
        Gson gson = new Gson();
        RideSimulationDTO rideSimulationDTO = gson.fromJson(mvcResult.getResponse().getContentAsString(), RideSimulationDTO.class);
        assertEquals(VehicleStatus.INACTIVE, rideSimulationDTO.getVehicleStatus());
    }

    @Test
    void getRideSimulationVehicleDoesntHaveRide() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/simulation-ride/4"))
                .andExpect(status().isOk()).andReturn();
        Gson gson = new Gson();
        RideSimulationDTO rideSimulationDTO = gson.fromJson(mvcResult.getResponse().getContentAsString(), RideSimulationDTO.class);
        assertNull(rideSimulationDTO.getRide());
    }

    @Test
    void getRideSimulationVehicleHaveRideButItEnded() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/simulation-ride/1"))
                .andExpect(status().isOk()).andReturn();
        Gson gson = new Gson();
        RideSimulationDTO rideSimulationDTO = gson.fromJson(mvcResult.getResponse().getContentAsString(), RideSimulationDTO.class);
        assertNull(rideSimulationDTO.getRide());
    }

    @Test
    void getRideSimulationVehicleHaveRide() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/simulation-ride/6"))
                .andExpect(status().isOk()).andReturn();
        Gson gson = new Gson();
        RideSimulationDTO rideSimulationDTO = gson.fromJson(mvcResult.getResponse().getContentAsString(), RideSimulationDTO.class);
        System.out.println(rideSimulationDTO);
        assertNotNull(rideSimulationDTO.getRide());
    }

    @Test
    void getRideSimulationVehicleDoesntExist() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MvcResult mvcResult = mockMvc.perform(get(RIDE_URL_PREFIX + "/simulation-ride/999"))
                .andExpect(status().isOk()).andReturn();
        assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void cancelRideSuccess() throws Exception {
        String dto = new ObjectMapper().writeValueAsString("Health issues.");
        MvcResult mvcResult = mockMvc.perform(post(RIDE_URL_PREFIX + "/cancel/4")
                .contentType(MediaType.APPLICATION_JSON).content(dto))
                .andExpect(status().isOk()).andReturn();
        assertEquals("Ride successfully canceled.", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void cancelRideEmptyReason() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(RIDE_URL_PREFIX + "/cancel/4")
                .contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest()).andReturn();
        assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void cancelRideRideNotConfirmed() throws Exception {
        String dto = new ObjectMapper().writeValueAsString("Passengers are nowhere to be found");
        MvcResult mvcResult = mockMvc.perform(post(RIDE_URL_PREFIX + "/cancel/5")
                        .contentType(MediaType.APPLICATION_JSON).content(dto))
                .andExpect(status().isBadRequest()).andReturn();
        assertEquals("Ride status must be confirmed to be able to cancel.", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void vehicleReportSuccess() throws Exception {
        List<DriverReport> reports = driverReportRepository.findByDriverId(5L);
        int numberOfReports = reports.size();
        MvcResult mvcResult = mockMvc.perform(post(VEHICLE_URL_PREFIX + "/report/driver/5")
                        .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2bGFkaW1pckBnbWFpbC5jb20iLCJyb2xlcyI6WyJDTElFTlQiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4NDQzL2FwaS91c2VyL2xvZ2luIiwiZXhwIjoxNjc2NzE1OTE3fQ.x4NbQlvuVIAIY5p5-ElLwB5fO1Pk5m9ztm9beHkRIa0"))
                .andExpect(status().isOk()).andReturn();
        assertEquals("Successfully reported the driver.", mvcResult.getResponse().getContentAsString());
        List<DriverReport> reportsPlusOne = driverReportRepository.findByDriverId(5L);
        int numberOfReportsPlusOne = reportsPlusOne.size();
        assertEquals(1, numberOfReportsPlusOne - numberOfReports);
    }

    @Test
    void vehicleReportDriverDoesntExist() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post(VEHICLE_URL_PREFIX + "/report/driver/999")
                .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2bGFkaW1pckBnbWFpbC5jb20iLCJyb2xlcyI6WyJDTElFTlQiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4NDQzL2FwaS91c2VyL2xvZ2luIiwiZXhwIjoxNjc2NzE1OTE3fQ.x4NbQlvuVIAIY5p5-ElLwB5fO1Pk5m9ztm9beHkRIa0"))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void vehicleReportAuthorizationHeaderDoesntExist() throws Exception {
        List<DriverReport> reports = driverReportRepository.findByDriverId(5L);
        int numberOfReports = reports.size();
        MvcResult mvcResult = mockMvc.perform(post(VEHICLE_URL_PREFIX + "/report/driver/5"))
                .andExpect(status().isBadRequest()).andReturn();
        List<DriverReport> reportsAfter = driverReportRepository.findByDriverId(5L);
        int numberOfReportsAfter = reportsAfter.size();
        assertEquals(numberOfReports, numberOfReportsAfter);
    }


    @Test
    void successfulUpdateDriverLongitudeAndLatitude() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(99.1);
        locationDTO.setLongitude(99.9);
        String dto = new ObjectMapper().writeValueAsString(locationDTO);
        mockMvc.perform(put(RIDE_URL_PREFIX + "/update-location/1").contentType(MediaType.APPLICATION_JSON).content(dto
                ))
                .andExpect(status().isOk());
        Optional<Vehicle> vehicle = vehicleRepository.findById(1L);
        assertEquals(locationDTO.getLongitude(), vehicle.get().getLongitude());
        assertEquals(locationDTO.getLatitude(), vehicle.get().getLatitude());
    }


}