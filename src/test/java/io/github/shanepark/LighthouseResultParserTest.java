package io.github.shanepark;

import io.github.shanepark.domain.ParseResult;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class LighthouseResultParserTest {

    @Test
    void start() {
        // Given
        File inputFolder = new File("src/test/resources/input");
        File outputFolder = new File("src/test/resources/output");
        double clsTarget = 0.05;

        // When
        LighthouseResultParser lighthouseResultParser = new LighthouseResultParser(inputFolder, outputFolder, clsTarget);
        ParseResult parseResult = lighthouseResultParser.parse();

        // Then
        assertThat(parseResult.passList()).hasSize(1);
        assertThat(parseResult.passList()).containsExactly(5);

        assertThat(parseResult.failList()).hasSize(1);
        assertThat(parseResult.failList()).containsExactly(2);

        assertThat(parseResult.invalidList()).hasSize(1);
        assertThat(parseResult.invalidList()).containsExactly(1);

        File[] outputFiles = outputFolder.listFiles();
        assertThat(outputFiles).hasSize(1);
        File outputFile = outputFiles[0];
        assertThat(outputFile.getName()).isEqualTo("2.html");

        for (File file : outputFiles) {
            file.delete();
        }
        outputFolder.delete();

    }
}
