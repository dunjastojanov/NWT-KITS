package com.uber.rocket.ride_booking.utils.user;

import com.uber.rocket.entity.ride.Passenger;
import com.uber.rocket.entity.user.User;

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
}
