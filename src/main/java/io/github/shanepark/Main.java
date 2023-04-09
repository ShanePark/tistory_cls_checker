package io.github.shanepark;

import io.github.shanepark.args.ArgsException;
import io.github.shanepark.args.ArgsParseResult;

import java.io.File;

public class Main {

    public static void main(String[] args) throws ArgsException {
        ArgsParseResult argsParseResult = new ArgsParseResult(args);
        File inputFolder = argsParseResult.getInputFolder();
        File outputFolder = argsParseResult.getOutputFolder();
        double clsTarget = argsParseResult.getClsTarget();

        LighthouseResultParser lighthouseResultParser = new LighthouseResultParser(inputFolder, outputFolder, clsTarget);
        lighthouseResultParser.parse();
    }


}
