package com.example.springsecurityexample.match;

import com.example.springsecurityexample.match.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
@EnableScheduling
@RequiredArgsConstructor
public class MatchScheduledTask {

    private final MatchService matchService;

    // 10분마다 실행
    @Scheduled(fixedRate = 600000)
    public void DeleteExpiredMatches() {
        matchService.DeleteRejectedMatches();
        matchService.DeleteFalseMatches();
    }

}
