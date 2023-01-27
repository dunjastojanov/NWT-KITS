package com.uber.rocket.configuration;

import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.*;
import com.uber.rocket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class Config {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    RideRepository rideRepository;
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    DestinationRepository destinationRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Bean
    CommandLineRunner configureTestData() {
        return args -> {
            Role clientRole = addRole("CLIENT");
            Role driverRole = addRole("DRIVER");
            Role adminRole = addRole("ADMINISTRATOR");

            addUser(adminRole, "Biljana", "Radulov", "0611111111");

            User driver1 = addUser(driverRole, "Dragan", "Dragic", "0611111111");
            Vehicle vehicle1 = addVehicle(driver1);
            User driver2 = addUser(driverRole, "Ivana", "Berkovic", "0611111111");
            Vehicle vehicle2 = addVehicle(driver2);
            User driver3 = addUser(driverRole, "Milan", "Nikolic", "0611111111");
            Vehicle vehicle3 = addVehicle(driver3);

            User client1 = addUser(clientRole, "Slavica", "Slavic", "0611111111");
            User client2 = addUser(clientRole, "Bogdana", "Vujic", "0611111111");
            User client3 = addUser(clientRole, "Vladimir", "Sinik", "0611111111");
            User client4 = addUser(clientRole, "Dalibor", "Dobrilovic", "0611111111");
            User client5 = addUser(clientRole, "Eleonora", "Desnica", "0611111111");
            User client6 = addUser(clientRole, "Jelena", "Stojanov", "0611111111");

            Ride ride1 = addRide(vehicle1, List.of(client1, client2), 276.0, 2252.9, "2023-01-25 14:15", "2023-01-25 17:15", 676, "_hfsGeg}wBxGtX|j@i]jBvMl@dMjBdm@uATLl@");
            addDestination(45.2571209, 19.8157059, "Hadzi Ruvimova 12 Novi Sad", ride1);
            addDestination(45.2478236, 19.804034, "Futoski put 18 Novi Sad", ride1);

            Ride ride2 = addRide(vehicle2, List.of(client3, client4), 180.0, 1538.2, "2023-01-24 21:20", "2023-01-25 00:20", 520, "smdsGwz~wBxAEA{EoFHkAf@_Al@`@gO@iES}NYyCgA_F`BeAz@_AdB{Dj@eBxGjGfBgF");
            addDestination(45.2477844, 19.8241151, "Brace Krkljus 7 Novi Sad", ride2);
            addDestination(45.2462784, 19.8346671, "Gogoljeva 18 Novi Sad", ride2);


            Ride ride3 = addRide(vehicle3, List.of(client5, client6), 360.0, 3039.4, "2023-01-25 20:03", "2023-01-25 21:03", 760, "kkcsGyubxBhAg@z\\jjB{OvFym@bA@zEmDHJ`P");
            addDestination(45.2423455,19.8437972,"Dr Ivana Ribara 13 Novi Sad",ride3);
            addDestination(45.2482226,19.8212104,"Rudjera Boskovica 22 Novi Sad",ride3);
        };
    }

    private Role addRole(String name) {
        Role role = new Role();
        role.setRole(name);
        return roleRepository.save(role);
    }

    private User addUser(Role role, String first_name, String last_name, String phoneNumber) {
        User user = new User();
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setEmail(first_name.toLowerCase() + "@gmail.com");
        user.setPassword(SecurityConfiguration.passwordEncoder().encode(first_name + "123"));
        user.setRoles(List.of(role));
        user.setPhoneNumber(phoneNumber);
        user.setBlocked(false);
        user.setCity("Novi Sad");
        user.setProfilePicture(null);
        user.setTokens((double) 0);
        return userRepository.save(user);
    }

    private Destination addDestination(double latitude, double longitude, String address, Ride ride) {
        Destination destination = new Destination();
        destination.setLatitude(latitude);
        destination.setLongitude(longitude);
        destination.setAddress(address);
        destination.setRide(ride);
        return destinationRepository.save(destination);
    }


    private Passenger addPassenger(User user) {
        Passenger passenger = new Passenger();
        passenger.setUser(user);
        passenger.setUserRidingStatus(UserRidingStatus.ACCEPTED);
        return passengerRepository.save(passenger);
    }

    private Vehicle addVehicle(User driver) {
        Vehicle vehicle = new Vehicle();

        vehicle.setLatitude(45.248130);
        vehicle.setLongitude(19.849070);
        vehicle.setPetFriendly(true);
        vehicle.setKidFriendly(true);
        vehicle.setStatus(VehicleStatus.ACTIVE);
        vehicle.setVehicleType(VehicleType.CARAVAN);
        vehicle.setDriver(driver);

        return vehicleRepository.save(vehicle);
    }


    private Ride addRide(Vehicle vehicle, List<User> users, Double duration, double length, String startTime, String endTime, int price, String routeLocation) {
        Ride ride = new Ride();

        ride.setStatus(RideStatus.CONFIRMED);
        ride.setKidFriendly(true);
        ride.setPetFriendly(true);
        ride.setDuration(duration);
        ride.setLength(length);
        ride.setVehicleTypeRequested(VehicleType.CARAVAN);
        ride.setStartTime(LocalDateTime.parse(startTime, formatter));
        ride.setEndTime(LocalDateTime.parse(endTime, formatter));
        ride.setPrice(price);
        ride.setRouteLocation(routeLocation);
        ride.setSplitFare(false);
        ride.setNow(true);

        ride = rideRepository.save(ride);

        ride.setPassengers(users.stream().map(this::addPassenger).toList());
        ride.setVehicle(vehicle);

        return rideRepository.save(ride);

    }
}
