package io.github.shanepark.domain;

import java.time.LocalDateTime;
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

        long totalNanoSecond = LocalDateTime.now().getNano() - start.getNano();
        long totalMilliSecond = totalNanoSecond / 1000000;
        System.out.println("\nFinished Parsing in " + getTotalTimeString(totalMilliSecond));
        System.out.println("====================================");
        System.out.println("Pass: " + passList);
        System.out.println("Fail: " + failList);
        System.out.println("Invalid: " + invalidList);
        System.out.println("CLS Average: " + clsAverage);
        System.out.println("====================================");
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
