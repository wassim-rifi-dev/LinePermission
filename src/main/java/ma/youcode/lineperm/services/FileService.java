package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import ma.youcode.lineperm.constants.FilePaths;

public class FileService {

    public void touch(String fileName) {
        try {
            Path file = Path.of(FilePaths.mainFilesDiractories + fileName);

            Files.createFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}