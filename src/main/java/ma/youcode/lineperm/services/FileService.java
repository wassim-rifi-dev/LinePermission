package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import ma.youcode.lineperm.constants.FilePaths;
import ma.youcode.lineperm.exceptions.FileAlreadyExisteException;

public class FileService {

    public void touch(String fileName) {
        try {
            if (UserService.files.containsKey(fileName)) {
                throw new FileAlreadyExisteException("Ce file est existe.");
            }

            Path filesFile = Path.of(FilePaths.filesFile);
            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);

            String fileWriting = "rwd|--- " + AuthService.currentUser + " " + fileName;

            Files.createFile(filePath);

            Files.writeString(filesFile , fileWriting + System.lineSeparator(), StandardOpenOption.APPEND);

            UserService.files.put(fileName, new String[]{AuthService.currentUser , "rwd|---"});
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String ls() {
        try {
            Path filesFile = Path.of(FilePaths.filesFile);

            String content = Files.readString(filesFile);

            return content;
        }  catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String cat(String fileName) {
        try {
            // Path filesFile = Path.of(FilePaths.filesFile);
            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);

            String content = Files.readString(filePath);

            return content;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}