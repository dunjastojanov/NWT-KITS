package com.uber.rocket.repository;

import com.uber.rocket.dto.UserDataDTO;
import com.uber.rocket.entity.user.User;
import com.uber.rocket.entity.user.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findFirstByDriver(User driver);

    @Query("FROM Vehicle vehicle WHERE vehicle.driver.id = :id")
    Vehicle findFirstByDriverId(@Param("id") Long id);
}
