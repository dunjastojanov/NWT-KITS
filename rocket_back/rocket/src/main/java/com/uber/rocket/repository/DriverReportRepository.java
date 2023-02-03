package com.uber.rocket.repository;

import com.uber.rocket.entity.user.DriverReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverReportRepository extends JpaRepository<DriverReport, Long> {
    List<DriverReport> findByDriverId(Long driverId);
}
