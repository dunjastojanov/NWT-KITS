package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.UserRepository;
import com.uber.rocket.ride_booking.utils.ride.RideCreationService;
import com.uber.rocket.ride_booking.utils.user.UserCreationService;
import com.uber.rocket.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.uber.rocket.ride_booking.utils.ride.RideCreationService.*;
import static com.uber.rocket.ride_booking.utils.user.UserCreationService.getGoodUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    private RideCreationService rideCreationService;
    @BeforeEach
    void initMocks() {
        closeable = openMocks(this);
        rideCreationService=new RideCreationService();
    }

    @Test
    @DisplayName("Negative test with wrong email")
    void getUserByEmailTest1() {
        when(this.userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserByEmail(anyString()));
    }

    @Test
    @DisplayName("Positive test with right email")
    void getUserByEmailTest2() {
        User user = getGoodUser();
        when(this.userRepositoryMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertEquals(user.toString(), userService.getUserByEmail(user.getEmail()).toString());
    }


    @Test
    @DisplayName("Ride isn't split fare,passenger doesn't have money")
    void checkPassengersTokensTest1() {
        Ride ride = rideCreationService.getRideSplitFareFalseClientHasNoMoney();
        assertFalse(userService.checkPassengersTokens(ride));
    }

    @Test
    @DisplayName("Ride isn't split fare,passenger has money")
    void checkPassengersTokensTest2() {
        Ride ride = rideCreationService.getRideSplitFareFalseClientHasMoney();
        assertTrue(userService.checkPassengersTokens(ride));
    }

    @Test
    @DisplayName("Ride is split fare,passenger has no money")
    void checkPassengersTokensTest3() {
        Ride ride = rideCreationService.getRideSplitFareTrueClientsHasNoMoney();
        assertFalse(userService.checkPassengersTokens(ride));
    }

    @Test
    @DisplayName("Ride is split fare,passenger has money")
    void checkPassengersTokensTest4() {
        Ride ride =rideCreationService.getRideSplitFareTrueClientsHasMoney();
        assertTrue(userService.checkPassengersTokens(ride));
    }


    @Test
    @DisplayName("Negative test wrong user id")
    void getByIdTest1() {
        when(this.userRepositoryMock.findById(anyLong())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> this.userService.getById(anyLong()));
    }

    @Test
    @DisplayName("Positive test right user id")
    void getByIdTest2() {
        when(this.userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(getGoodUser()));
        assertEquals(getGoodUser(), this.userService.getById(anyLong()));
    }


    @Test
    @DisplayName("Withdraw tokens for 1 client, no split fare ")
    void passengerTokensWithdrawTest1() {
        Ride ride = rideCreationService.getRideOnePassengerHasMoney();
        ride.setSplitFare(false);
        userService.passengerTokensWithdraw(ride);
        verify(this.userRepositoryMock, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Withdraw tokens for 2 client, has split fare")
    void passengerTokensWithdrawTest2() {
        Ride ride = rideCreationService.getRideMorePassengerYesMoney();
        ride.setSplitFare(true);
        userService.passengerTokensWithdraw(ride);
        verify(this.userRepositoryMock, times(ride.getPassengers().size())).save(any(User.class));
    }


}