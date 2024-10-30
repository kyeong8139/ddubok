package com.ddubok.api.scheduler;


import com.ddubok.api.card.repository.custom.CardRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduledTasks {

    private final CardRepositoryCustom cardRepositoryCustom;

    @Transactional
    @Scheduled(fixedRate = 300000)
    void cardOpen() {
        Long result = cardRepositoryCustom.updateCardStates();
        log.info("Card opened: " + result);
    }
}
