package io.github.shanepark.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

public record ParseResult(
        List<Integer> passList,
        List<Integer> failList,
        List<Integer> invalidList,
        double clsAverage
) {

    public void printResult(LocalDateTime start) {
        Collections.sort(passList);
        Collections.sort(failList);
        Collections.sort(invalidList);

        long millisecondsTaken = getTotalMilliSecondTaken(start);
        String totalTimeString = getTotalTimeString(millisecondsTaken);

        System.out.println("\nFinished Parsing in " + totalTimeString);
        System.out.println("====================================");
        System.out.println("Total Passed: " + passList.size());
        System.out.println("Pass: " + passList);
        System.out.println("Total Failed: " + failList.size());
        System.out.println("Fail: " + failList);
        System.out.println("Total Invalid: " + invalidList.size());
        System.out.println("Invalid: " + invalidList);
        System.out.println("CLS Average: " + clsAverage);
        System.out.println("====================================");
    }

    private static long getTotalMilliSecondTaken(LocalDateTime start) {
        LocalDateTime now = LocalDateTime.now();
        long endMillis = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long startMillis = start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return endMillis - startMillis;
    }

    private String getTotalTimeString(long totalMilliSecond) {
        if (totalMilliSecond < 1000) {
            return totalMilliSecond + "ms";
        }
        double totalSecond = totalMilliSecond / 1000.0;
        if (totalMilliSecond < 60000) {
            return totalSecond + "s";
        }
        double totalMinute = totalSecond / 60.0;
        return totalMinute + "m";
    }
}
