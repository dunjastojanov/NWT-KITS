package com.uber.rocket.ride_booking.utils.user;

import com.uber.rocket.dto.ChangeStatusDTO;
import com.uber.rocket.dto.UserDTO;
import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.ride.UserRidingStatus;
import com.uber.rocket.entity.user.*;

import java.util.ArrayList;
import java.util.List;

public class UserCreationService {

    public static User getGoodUser() {
        User user = new User();
        user.setEmail("jelena@gmail.com");
        user.setId(1L);
        return user;
    }

    public static User getUserWithNoMoney() {
        User user = new User();
        user.setTokens((double) 0);
        return user;
    }

    public static User getUserWithMoney() {
        User user = new User();
        user.setTokens((double) 1000);
        return user;
    }

    public static Passenger getPassengerWithNoMoney() {
        Passenger passenger = new Passenger();
        passenger.setUser(getUserWithNoMoney());
        return passenger;
    }

    public static Passenger getPassengerWithMoney() {
        Passenger passenger = new Passenger();
        passenger.setUser(getUserWithMoney());
        return passenger;
    }

    public static User getDriver() {
        Role role = new Role(1L, RoleType.DRIVER.name());
        User user = new User();
        user.setRoles(new ArrayList<>());
        user.getRoles().add(role);
        user.setEmail("neki_email");
        user.setId(1L);
        return user;
    }

    public static List<Vehicle> getDriverVehicles() {
        List<Vehicle> list = new ArrayList<>();
        list.add(getVehicle());
        return list;
    }

    public static Vehicle getVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setDriver(getDriver());
        vehicle.setStatus(VehicleStatus.ACTIVE);
        return vehicle;
    }

    public static User getClient() {
        Role role = new Role(1L, RoleType.CLIENT.name());
        User user = new User();
        user.setRoles(new ArrayList<>());
        user.getRoles().add(role);
        user.setId(1L);
        return user;
    }

    public static User getGoodClient() {
        Role role = new Role(1L, RoleType.CLIENT.name());
        User user = new User();
        user.setRoles(new ArrayList<>());
        user.getRoles().add(role);
        user.setId(1L);
        user.setFirstName("Jelena");
        user.setLastName("Stojanov");
        user.setEmail("jelena@gmail.com");
        user.setProfilePicture("profile_picture");
        return user;
    }

    public static UserDTO getGoodClientDTO() {
        Role role = new Role(1L, RoleType.CLIENT.name());
        UserDTO user = new UserDTO();
        user.setRole("CLIENT");
        user.setId(1L);
        user.setFirstName("Jelena");
        user.setLastName("Stojanov");
        user.setEmail("jelena@gmail.com");
        user.setProfilePicture("profile_picture");
        return user;
    }

    public static User getAdminUser() {
        Role role = new Role(1L, RoleType.ADMINISTRATOR.name());
        User user = new User();
        user.setRoles(new ArrayList<>());
        user.getRoles().add(role);
        user.setId(1L);
        return user;
    }

    public static ChangeStatusDTO getChangeStatus() {
        return new ChangeStatusDTO();
    }

    public static ChangeStatusDTO getGoodChangeStatus() {
        ChangeStatusDTO changeStatusDTO = new ChangeStatusDTO();
        changeStatusDTO.setRidingStatus(UserRidingStatus.DENIED);
        changeStatusDTO.setUserId("1");
        return changeStatusDTO;
    }
}
