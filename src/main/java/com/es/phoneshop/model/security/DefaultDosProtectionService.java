package com.es.phoneshop.model.security;


import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {

    private static final long THRESHOLD = 20;
    private static final long DELAY = 1000 * 60;
    public static final long PERIOD = 1000 * 60;
    private Map<String, Long> countOfRequests = new ConcurrentHashMap<>();

    @Override
    public boolean isAllowed(String ip) {
        Long count = countOfRequests.get(ip);
        if (count == null) {
            count = 1L;
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
        }
        countOfRequests.put(ip, count);
        return true;
    }

    private DefaultDosProtectionService() {
        TimerTask task = new TimerTask() {
            public void run() {
                countOfRequests.clear();
            }
        };
        Timer timer = new Timer("Timer", true);
        timer.schedule(task, DELAY, PERIOD);
    }

    public static DefaultDosProtectionService getInstance() {
        return DefaultDosProtectionService.Holder.INSTANCE;
    }

    private static class Holder {
        static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }
}
