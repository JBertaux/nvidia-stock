package be.jeromebertaux.nvidiastock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskRunner {

    private final StockPoller stockPoller;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void run() {
        log.info("Start execution");
        try {
            stockPoller.poll();
        } catch (Exception e) {
            log.error("Cannot run the task", e);
        }
        log.info("End execution");
    }

}
