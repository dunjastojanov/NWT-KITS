package com.uber.rocket.service;

import com.uber.rocket.entity.user.LogInfo;
import com.uber.rocket.repository.LogInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LogInfoService {
    @Autowired
    private LogInfoRepository logInfoRepository;

    public long getWorkingHoursInPrevious24Hours(Long driverId) {
        List<LogInfo> list = logInfoRepository.findAllLogsInSinceLast24Hours(driverId);
        Duration totalDuration = Duration.ZERO;
        for (LogInfo loginfo :
                list) {
            Duration duration;
            if (loginfo.getEnding() != null) {
                duration = Duration.between(loginfo.getBegging(), loginfo.getEnding());
            } else {
                duration = Duration.between(loginfo.getBegging(), LocalDateTime.now());
            }
            totalDuration = totalDuration.plus(duration);
        }
        return totalDuration.get(ChronoUnit.HOURS);
    }

    public boolean hasDriverExceededWorkingHours(Long driverId) {
        return getWorkingHoursInPrevious24Hours(driverId) >= 8;
    }

    public void startCountingHours(Long driverId) {
        LogInfo logInfo = new LogInfo();
        logInfo.setBegging(LocalDateTime.now());
        logInfo.setUserId(driverId);
        logInfo.setEnding(null);
        logInfoRepository.save(logInfo);
    }

    public void endWorkingHourCount(Long driverId) {
        LogInfo logInfo = logInfoRepository.findFirstByUserIdIsOrderByBeggingDesc(driverId);
        logInfo.setEnding(LocalDateTime.now());
        logInfoRepository.save(logInfo);
    }


}
