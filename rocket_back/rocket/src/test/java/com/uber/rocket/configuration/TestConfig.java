package com.uber.rocket.configuration;

import com.uber.rocket.entity.ride.*;
import com.uber.rocket.entity.user.*;
import com.uber.rocket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@TestConfiguration
@Profile("test")
public class TestConfig {
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

    @Autowired

    LogInfoRepository logInfoRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Bean
    public CommandLineRunner configureTestData() {
        return args -> {
            System.out.println("aaaaaaaaaaaaaaaaaaaa");
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
            User client7 = addUser(clientRole, "Marko", "Desnica", "0611111111");
            User client8 = addUser(clientRole, "Helena", "Stojanov", "0611111111");
            User client9 = addUser(clientRole, "Jovica", "Vujic", "0611111111");


            Ride ride1 = addRide(vehicle1, List.of(client1, client2), 276.0, 2252.9, "2023-01-25 14:15", "2023-01-25 17:15", 676, "_hfsGeg}wBxGtX|j@i]jBvMl@dMjBdm@uATLl@");
            addDestination(45.2571209, 19.8157059, "Hadzi Ruvimova 12 Novi Sad aloo breee", ride1);
            addDestination(45.2478236, 19.804034, "Futoski put 18 Novi Sad", ride1);

            Ride ride2 = addRide(vehicle2, List.of(client3, client4), 180.0, 1538.2, "2023-01-24 21:20", "2023-01-25 00:20", 520, "smdsGwz~wBxAEA{EoFHkAf@_Al@`@gO@iES}NYyCgA_F`BeAz@_AdB{Dj@eBxGjGfBgF");
            addDestination(45.2477844, 19.8241151, "Brace Krkljus 7 Novi Sad", ride2);
            addDestination(45.2462784, 19.8346671, "Gogoljeva 18 Novi Sad", ride2);


            Ride ride3 = addRide(vehicle3, List.of(client5, client6), 360.0, 3039.4, "2023-01-25 20:03", "2023-01-25 21:03", 760, "kkcsGyubxBhAg@z\\jjB{OvFym@bA@zEmDHJ`P");
            addDestination(45.2423455, 19.8437972, "Dr Ivana Ribara 13 Novi Sad", ride3);
            addDestination(45.2482226, 19.8212104, "Rudjera Boskovica 22 Novi Sad", ride3);

            Ride ride4 = addRide(vehicle3, List.of(client5), 876.0, 7331.1, "2023-02-02 18:49", null, 1276, "imfsGiguwB`BnFL{@d@aCvB`AfLfEv@XbA\\HBLDzLrEb@wC^qCJy@b@iDHq@f@qD\\_C@K\\yB\\aCD[ZqBHm@F[@K`@gCLw@^cC@Gf@{CBKVeBHa@T}AXeBd@yDDa@d@mDBS@I@GH?HEFGBI@I@IAKCI?c@@MD_@VsBX}BLgABQHu@F{@@w@@a@Eg@GWGSCOIYkAiFI[_AiE_@{AAIACGYI]QJQJC@i@XoBdAsFxCeDfBIDWNuDpBs@`@EBOJEMAG[sAs@uC{E}RWaAAEAEGWEQESAEw@}CqDgOkAyEk@_CKa@g@uBOm@WcAGUCMEQEQi@yBq@qCu@wCSy@]yAcB_Hq@uCmA_Fi@yBAKAM?W@Q?W@E?G?IAIAGCGCGCEEEAAQMMMEECGEIGSGOKc@u@yCg@oBAEAEGUGSAG{BmIACAEIUIQCESe@[q@q@wAkDiHqAkCiAcCCEACM[LQBCr@{@~@gA`AkATYDE@AV]@EAIAISc@ACSc@s@yAO]Ue@[u@Qg@ACAEEQESAEGUOiAIcAAiA?e@?[DcBHmD@g@?O?E@E@c@@W?G@a@LiFFkDDiC?sB?WEmAKyAOuBSeC?GACI}@Ge@Ss@GQWw@sAkEHGBCJEr@[lCmA`@Q~@c@Nx@`@Sh@nCFEzEyB");
            ride4.setStatus(RideStatus.CONFIRMED);
            rideRepository.save(ride4);

            addDestination(19.7747737, 45.2579691, "Avrama Miletica bb", ride4);
            addDestination(19.2615881, 45.2615881, "Bulevar oslobodjenja 25", ride4);

            Ride ride5 = addRide(vehicle2, List.of(client5), 588.0, 4907.9, "2023-02-02 19:06", null, 988, "_vbsG}q`xB??@F@DNv@DT@F`@xBr@xDJj@@JfA`GHd@DRrAfHRbAPx@FTQJUJkBp@kDnAC?C@C@MDGBGBKBIBEB_AZo@TG@EBWFQ@DXFZ@HPhADXL`BDj@Bh@?f@?d@AR?DABERGPCDCFAFAH?F@H@F@FBFBDB@AH?F?H?D?F?JCb@GjACh@?TDl@Bd@APOtCALELCJCHCNQpDCZg@rKCr@?DA@Cj@Cf@?FKpBQbEAZGhAe@jKCl@AL?HC^AVC`@?HM~Bk@jMMvCAHCh@Ch@?FAJKfCYrGC\\Cr@GfAInBCf@AHAR?NANC^?HOxC[zGI|AMnCAL?HAVEt@?HM|BKfCCd@Gh@Or@Qj@]p@c@`@a@^cClAcDbBEBYPQH]RE@iKrFYP]NmE~B}Ax@o@^e@VA?CBC@QHQJQJC@i@XoBdAsFxCeDfBIDWNuDpBs@`@EBOJEMAG[sAs@uC{E}R");
            ride5.setStatus(RideStatus.STARTED);
            rideRepository.save(ride5);

            addDestination(19.8327891, 45.2388761, "Narodnog Fronta 111", ride5);
            addDestination(19.7938139, 45.2550643, "Bulevar Vojvode Stepe", ride5);

            Ride ride6 = addRide(vehicle2, List.of(client4, client2), 588.0, 4907.9, "2023-02-02 19:06", null, 988, "_vbsG}q`xB??@F@DNv@DT@F`@xBr@xDJj@@JfA`GHd@DRrAfHRbAPx@FTQJUJkBp@kDnAC?C@C@MDGBGBKBIBEB_AZo@TG@EBWFQ@DXFZ@HPhADXL`BDj@Bh@?f@?d@AR?DABERGPCDCFAFAH?F@H@F@FBFBDB@AH?F?H?D?F?JCb@GjACh@?TDl@Bd@APOtCALELCJCHCNQpDCZg@rKCr@?DA@Cj@Cf@?FKpBQbEAZGhAe@jKCl@AL?HC^AVC`@?HM~Bk@jMMvCAHCh@Ch@?FAJKfCYrGC\\Cr@GfAInBCf@AHAR?NANC^?HOxC[zGI|AMnCAL?HAVEt@?HM|BKfCCd@Gh@Or@Qj@]p@c@`@a@^cClAcDbBEBYPQH]RE@iKrFYP]NmE~B}Ax@o@^e@VA?CBC@QHQJQJC@i@XoBdAsFxCeDfBIDWNuDpBs@`@EBOJEMAG[sAs@uC{E}R");
            ride6.setStatus(RideStatus.REQUESTED);

            rideRepository.save(ride6);
            addDestination(19.8327891, 45.2388761, "Narodnog Fronta 111", ride6);
            addDestination(19.7938139, 45.2550643, "Bulevar Vojvode Stepe", ride6);


            Ride ride7 = addRide(vehicle2, List.of(client7, client8), 588.0, 4907.9, "2023-02-02 19:06", null, 988, "_vbsG}q`xB??@F@DNv@DT@F`@xBr@xDJj@@JfA`GHd@DRrAfHRbAPx@FTQJUJkBp@kDnAC?C@C@MDGBGBKBIBEB_AZo@TG@EBWFQ@DXFZ@HPhADXL`BDj@Bh@?f@?d@AR?DABERGPCDCFAFAH?F@H@F@FBFBDB@AH?F?H?D?F?JCb@GjACh@?TDl@Bd@APOtCALELCJCHCNQpDCZg@rKCr@?DA@Cj@Cf@?FKpBQbEAZGhAe@jKCl@AL?HC^AVC`@?HM~Bk@jMMvCAHCh@Ch@?FAJKfCYrGC\\Cr@GfAInBCf@AHAR?NANC^?HOxC[zGI|AMnCAL?HAVEt@?HM|BKfCCd@Gh@Or@Qj@]p@c@`@a@^cClAcDbBEBYPQH]RE@iKrFYP]NmE~B}Ax@o@^e@VA?CBC@QHQJQJC@i@XoBdAsFxCeDfBIDWNuDpBs@`@EBOJEMAG[sAs@uC{E}R");
            ride7.setStatus(RideStatus.REQUESTED);
            ride7.getPassengers().forEach(passenger -> {
                passenger.setUserRidingStatus(UserRidingStatus.DENIED);
                passengerRepository.save(passenger);
            });
            rideRepository.save(ride7);
            addDestination(19.8327891, 45.2388761, "Narodnog Fronta 111", ride7);
            addDestination(19.7938139, 45.2550643, "Bulevar Vojvode Stepe", ride7);

            Ride ride8 = addRide(vehicle1, List.of(client7, client8), 588.0, 4907.9, "2023-02-02 19:06", null, 988, "_vbsG}q`xB??@F@DNv@DT@F`@xBr@xDJj@@JfA`GHd@DRrAfHRbAPx@FTQJUJkBp@kDnAC?C@C@MDGBGBKBIBEB_AZo@TG@EBWFQ@DXFZ@HPhADXL`BDj@Bh@?f@?d@AR?DABERGPCDCFAFAH?F@H@F@FBFBDB@AH?F?H?D?F?JCb@GjACh@?TDl@Bd@APOtCALELCJCHCNQpDCZg@rKCr@?DA@Cj@Cf@?FKpBQbEAZGhAe@jKCl@AL?HC^AVC`@?HM~Bk@jMMvCAHCh@Ch@?FAJKfCYrGC\\Cr@GfAInBCf@AHAR?NANC^?HOxC[zGI|AMnCAL?HAVEt@?HM|BKfCCd@Gh@Or@Qj@]p@c@`@a@^cClAcDbBEBYPQH]RE@iKrFYP]NmE~B}Ax@o@^e@VA?CBC@QHQJQJC@i@XoBdAsFxCeDfBIDWNuDpBs@`@EBOJEMAG[sAs@uC{E}R");
            ride8.setStatus(RideStatus.REQUESTED);
            rideRepository.save(ride8);
            addDestination(19.8327891, 45.2388761, "Narodnog Fronta 111", ride8);
            addDestination(19.7938139, 45.2550643, "Bulevar Vojvode Stepe", ride8);

            vehicle1.setStatus(VehicleStatus.ACTIVE);
            vehicleRepository.save(vehicle1);



            LogInfo logInfo1 = addLogInfo(driver1);
        };
    }

    private Role addRole(String name) {
        Role role = new Role();
        role.setRole(name);
        return roleRepository.save(role);
    }

    private LogInfo addLogInfo(User driver) {
        LogInfo logInfo = new LogInfo();
        logInfo.setUserId(driver.getId());
        logInfo.setBegging(LocalDateTime.of(2023, 1, 28, 10, 0));
        logInfo.setEnding(null);
        return logInfoRepository.save(logInfo);
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
        user.setTokens((double) 1000);
        user = userRepository.save(user);
        user.setProfilePicture("http://localhost:8443/images/" + user.getId() + "/" + user.getFirstName() + ".jpg");
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
        vehicle.setStatus(VehicleStatus.INACTIVE);
        vehicle.setVehicleType(VehicleType.CARAVAN);
        vehicle.setDriver(driver);

        return vehicleRepository.save(vehicle);
    }


    private Ride addRide(Vehicle vehicle, List<User> users, Double duration, double length, String startTime, String endTime, int price, String routeLocation) {
        Ride ride = new Ride();

        ride.setStatus(RideStatus.ENDED);
        ride.setKidFriendly(true);
        ride.setPetFriendly(true);
        ride.setDuration(duration);
        ride.setLength(length);
        ride.setVehicleTypeRequested(VehicleType.CARAVAN);
        ride.setStartTime(LocalDateTime.parse(startTime, formatter));
        if (endTime == null)
            ride.setEndTime(null);
        else
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
