package tech.petrepopescu.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReadFileUtil {
    public static List<String> readLines(File file, Charset charset) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        Path path = file.toPath();
        return Files.readAllLines(path, charset);
    }

    public static String readFileToString(File file, Charset charset) throws IOException {
        return String.join("\n", readLines(file, charset)) + "\n";
    }
}
