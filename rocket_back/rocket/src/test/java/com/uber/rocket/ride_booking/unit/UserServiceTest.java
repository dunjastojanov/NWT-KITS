package com.uber.rocket.ride_booking.unit;

import com.uber.rocket.entity.ride.Ride;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.repository.UserRepository;
import com.uber.rocket.ride_booking.utils.user.UserCreationService;
import com.uber.rocket.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.uber.rocket.ride_booking.utils.ride.RideCreationService.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private UserService userService;

    private AutoCloseable closeable;

    @BeforeEach
    void initMocks() {
        closeable = openMocks(this);
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
        User user = UserCreationService.getGoodUser();
        when(this.userRepositoryMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertEquals(user.toString(), userService.getUserByEmail(user.getEmail()).toString());
    }


    @Test
    @DisplayName("Ride isn't split fare,passenger doesn't have money")
    void checkPassengersTokensTest1() {
        Ride ride=getRideSplitFareFalseClientHasNoMoney();
        assertFalse(userService.checkPassengersTokens(ride));
    }

    @Test
    @DisplayName("Ride isn't split fare,passenger has money")
    void checkPassengersTokensTest2() {
        Ride ride=getRideSplitFareFalseClientHasMoney();
        assertTrue(userService.checkPassengersTokens(ride));
    }

    @Test
    @DisplayName("Ride is split fare,passenger has no money")
    void checkPassengersTokensTest3() {
        Ride ride=getRideSplitFareTrueClientsHasNoMoney();
        assertFalse(userService.checkPassengersTokens(ride));
    }
    @Test
    @DisplayName("Ride is split fare,passenger has money")
    void checkPassengersTokensTest4() {
        Ride ride=getRideSplitFareTrueClientsHasMoney();
        assertTrue(userService.checkPassengersTokens(ride));
    }



}