package io.github.shanepark;

import io.github.shanepark.domain.ParseResult;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class LighthouseResultParser {

    private final File inputFolder;
    private final File outputFolder;
    private final double clsTarget;

    List<Integer> pass = new ArrayList<>();
    List<Integer> fail = new ArrayList<>();
    List<Integer> invalid = new ArrayList<>();
    double clsTotal = 0;

    public ParseResult parse() {
        LocalDateTime start = LocalDateTime.now();
        File[] files = inputFolder.listFiles();

        for (File file : files) {
            if (!isValid(file))
                continue;
            try {
                processFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error while processing file: " + file.getName());
            }
        }

        double clsAverage = clsTotal / (pass.size() + fail.size());
        ParseResult parseResult = new ParseResult(pass, fail, invalid, clsAverage);

        parseResult.printResult(start);
        return parseResult;
    }

    private ProcessResult processFile(File file) throws IOException {
        String scriptContent = getScriptContent(file);
        JSONObject jsonData = getJsonData(scriptContent);

        String requestedUrl = jsonData.getString("requestedUrl");
        int urlIndex = getUrlIndex(requestedUrl);

        if (jsonData.has("runtimeError")) {
            invalid.add(urlIndex);
            return ProcessResult.INVALID;
        }

        double clsScore = getClsScore(jsonData);
        clsTotal += clsScore;
        System.out.println("URL: " + requestedUrl + ", CLS: " + clsScore);

        if (!outputFolder.exists())
            outputFolder.mkdirs();

        if (clsTarget <= clsScore) {
            fail.add(urlIndex);
            copyHtmlFileToOutputFolder(file, urlIndex);
            return ProcessResult.FAIL;
        }


        pass.add(urlIndex);
        return ProcessResult.PASS;
    }

    private void copyHtmlFileToOutputFolder(File file, int urlIndex) throws IOException {
        File copyFile = new File(outputFolder, urlIndex + ".html");
        if (copyFile.exists()) {
            copyFile.delete();
        }
        Files.copy(file.toPath(), copyFile.toPath());
    }

    private static double getClsScore(JSONObject jsonData) {
        JSONObject audits = jsonData.getJSONObject("audits");
        JSONObject clsResult = audits.getJSONObject("cumulative-layout-shift");
        double value = clsResult.getDouble("numericValue");
        return Math.round(value * 1000.0) / 1000.0;
    }

    private int getUrlIndex(String requestedUrl) {
        String[] split = requestedUrl.split("/");
        String last = split[split.length - 1];
        return Integer.parseInt(last);
    }

    private JSONObject getJsonData(String scriptContent) {
        if (!scriptContent.startsWith("window.__LIGHTHOUSE_JSON__ = ")) {
            throw new IllegalArgumentException("Invalid script content");
        }

        String jsonDataString = scriptContent.substring(29);
        return new JSONObject(jsonDataString);
    }

    private static String getScriptContent(File file) throws IOException {
        String htmlContent = new String(Files.readAllBytes(file.toPath()));
        Document document = Jsoup.parse(htmlContent);
        Element scriptTag = document.selectFirst("script");
        String scriptContent = scriptTag.html();
        return scriptContent;
    }

    enum ProcessResult {
        PASS, FAIL, INVALID
    }

    private boolean isValid(File file) {
        if (file.isDirectory())
            return false;
        return file.getName().endsWith(".html");
    }

}
