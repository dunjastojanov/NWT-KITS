package com.uber.rocket.repository;

import com.uber.rocket.dto.UserDataDTO;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import com.uber.rocket.entity.user.VehicleStatus;
import com.uber.rocket.entity.user.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findFirstByDriver(User driver);

    @Query("FROM Vehicle vehicle WHERE vehicle.driver.id = :id")
    Vehicle findFirstByDriverId(@Param("id") Long id);

    @Query("SELECT v FROM Vehicle v WHERE v.status = :status AND v.vehicleType = :vehicleType AND v.petFriendly = :petFriendly AND v.kidFriendly = :kidFriendly AND v.driver.blocked = false")
    List<Vehicle> findByStatusAndTypeAndKidAndPetFriendly(@Param("status") VehicleStatus status, @Param("vehicleType") VehicleType vehicleType , @Param("kidFriendly") boolean kidFriendly, @Param("petFriendly") boolean petFriendly);

    @Query("SELECT v FROM Vehicle v WHERE v.status = com.uber.rocket.entity.user.VehicleStatus.ACTIVE")
    List<Vehicle> findByActiveStatus();
    List<Vehicle> findAllByStatusIs(VehicleStatus vehicleStatus);
}
