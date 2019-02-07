package com.nc.airport.backend.service.utils;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class ScheduledExecutor {
    private TaskScheduler taskScheduler = new DefaultManagedTaskScheduler();
    private Map<BigInteger, ScheduledFuture> ticketIdToScheduler = new HashMap<>();

    public ScheduledFuture deleteAfterDelay(BigInteger objectId, long seconds) {
        return null;
    }
}
