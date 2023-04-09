package io.github.shanepark.args;

public class ArgsException extends Exception {

    public ArgsException(String message) {
        System.err.println(message);
    }

    public ArgsException() {
        System.err.println("Usage: java -jar build/libs/tistory-blog-cls-checker-1.0-SNAPSHOT.jar \\\n" +
                "  --input=./results \\\n" +
                "  --output=./bad-cls-list \\\n" +
                "  --cls=0.03");
    }

}
