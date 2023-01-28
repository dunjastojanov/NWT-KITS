package com.uber.rocket.repository;

import com.uber.rocket.entity.user.LogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogInfoRepository extends JpaRepository<LogInfo,Long> {
    @Query("SELECT loginfo FROM LogInfo loginfo WHERE loginfo.userId = :driverId and DATE_SUB(CURRENT_TIMESTAMP,1) < loginfo.begging and loginfo.begging > CURRENT_TIMESTAMP")
    List<LogInfo> findAllLogsInSinceLast24Hours(@Param("driverId") Long driverId);

    LogInfo findFirstByUserIdIsOrderByBeggingDesc(Long driverId);

    List<LogInfo> findLogInfoByUserId(Long userId);
}
