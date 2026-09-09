package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import ma.youcode.lineperm.constants.FilePaths;

public class FileService {

    public void touch(String fileName) {
        try {
            Path filesFile = Path.of(FilePaths.filesFile);
            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);

            String fileWriting = "rwd|--- " + AuthService.currentUser + " " + fileName;

            Files.writeString(filesFile , fileWriting + System.lineSeparator() + StandardOpenOption.APPEND);

            Files.createFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}