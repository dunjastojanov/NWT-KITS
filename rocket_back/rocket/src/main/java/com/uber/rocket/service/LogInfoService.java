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
        List<LogInfo> list = logInfoRepository.findLogInfoByUserId(driverId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursAgo = now.minusHours(24);
        Duration totalDuration = Duration.ZERO;
        for (LogInfo obj : list) {
            if (obj.getBegging().isAfter(twentyFourHoursAgo) && obj.getBegging().isBefore(now)) {
                Duration duration;
                if (obj.getEnding() != null) {
                    duration = Duration.between(obj.getBegging(), obj.getEnding());
                }
                else {
                    duration = Duration.between(obj.getBegging(), LocalDateTime.now());
                }

                totalDuration = totalDuration.plus(duration);
            }
        }

        if (totalDuration.isZero()) {
            return 0;
        }
        return totalDuration.toHours();
    }

    public boolean hasDriverExceededWorkingHours(Long driverId) {
        long hours = getWorkingHoursInPrevious24Hours(driverId);
        return hours >= 8;
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
