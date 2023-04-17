package bntu.fitr.gorbachev.ticketsgenerator.main.test;

import com.google.protobuf.ByteString;

import java.io.*;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Test9 {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Я записал путь как в UNIX, и этот метод все равно сделал ее как путь в Windows
        Path path = Paths.get("src/main/java/");
        System.out.println(path.getFileName());
        System.out.println(path.toAbsolutePath());

        try (Stream<Path> entries = Files.walk(path, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)) {
            entries.filter(e -> e.toFile().isFile())
                    .forEachOrdered(p -> {
                        File f = p.toFile();
                        System.out.println(f);
                    });
        }

        Files.createTempFile("frex", ".java");
    }
}
