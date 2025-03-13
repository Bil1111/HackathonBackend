package com.example.config.statistics;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {
    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("completedTasks", 25);
        stats.put("quizzesTaken", 10);
        stats.put("badgesEarned", 5);
        stats.put("studyHours", 40);
        return stats;
    }
}
