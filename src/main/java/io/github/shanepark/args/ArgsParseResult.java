package io.github.shanepark.args;

import lombok.Getter;

import java.io.File;

@Getter
public class ArgsParseResult {

    private final File inputFolder;
    private final File outputFolder;
    private final double clsTarget;

    public ArgsParseResult(String[] args) throws ArgsException {
        String inputFolderArg = null;
        String outputFolderArg = null;
        String clsTargetArg = null;
        for (String arg : args) {
            if (arg.startsWith("--input=")) {
                inputFolderArg = arg.substring(8);
            } else if (arg.startsWith("--output=")) {
                outputFolderArg = arg.substring(9);
            } else if (arg.startsWith("--cls=")) {
                clsTargetArg = arg.substring(6);
            } else {
                throw new ArgsException("Invalid argument: " + arg);
            }
        }
        if (inputFolderArg == null || outputFolderArg == null || clsTargetArg == null) {
            throw new ArgsException();
        }

        File currentPath = new File(".");
        this.inputFolder = getFolder(inputFolderArg, currentPath);
        this.outputFolder = getFolder(outputFolderArg, currentPath);
        try {
            this.clsTarget = Double.parseDouble(clsTargetArg);
        } catch (NumberFormatException e) {
            throw new ArgsException("Invalid argument: " + clsTargetArg);
        }
        validate();
    }

    private File getFolder(String inputFolderArg, File currentPath) {
        if (inputFolderArg.startsWith("/"))
            return new File(inputFolderArg);
        return new File(currentPath, inputFolderArg);
    }

    public void validate() throws ArgsException {
        if (!inputFolder.exists() || !inputFolder.isDirectory()) {
            throw new ArgsException("Input folder does not exist");
        }
        if (inputFolder.listFiles().length == 0) {
            throw new ArgsException("Input folder is empty");
        }
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }
        System.out.println("Successfully parsed arguments");
        System.out.println("Input folder: " + inputFolder.getAbsolutePath());
        System.out.println("Output folder: " + outputFolder.getAbsolutePath());
        System.out.println("CLS target: " + clsTarget);
    }

}
