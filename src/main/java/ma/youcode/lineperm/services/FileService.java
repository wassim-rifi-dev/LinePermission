package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

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

    public void ls() {
        try {
            Path filesFile = Path.of(FilePaths.filesFile);

            String content = Files.readString(filesFile);

            System.out.println(content);
        }  catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void cat(String fileName) {
        try {
            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);

            if (!Files.exists(filePath)) {
                System.out.println("Aucune file avec se nom.");
            }

            String fileOwner = UserService.files.get(fileName)[0];
            String filePermission = UserService.files.get(fileName)[1];

            if (!fileOwner.equals(AuthService.currentUser)) {
                String[] permissionPart = filePermission.trim().split("\\|", 2);

                if (!permissionPart[1].contains("r")) {
                    System.out.println("Vous n'avez pas l'acces.");
                    return;
                }
            }

            String content = Files.readString(filePath);

            System.out.println(content);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void nano(String fileName) {
        try {
            Scanner scanner = new Scanner(System.in);

            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);

            if (!Files.exists(filePath)) {
                System.out.println("Aucune file avec se nom.");
            }

            String fileOwner = UserService.files.get(fileName)[0];
            String filePermission = UserService.files.get(fileName)[1];

            if (!fileOwner.equals(AuthService.currentUser)) {
                String[] permissionPart = filePermission.trim().split("\\|", 2);

                if (!permissionPart[1].contains("w")) {
                    System.out.println("Vous n'avez pas l'acces.");
                    return;
                }
            }

            System.out.println("Creer EOF pour terminer l'edit.\n");

            String content = Files.readString(filePath);
            System.out.print(content);

            String newContent;

            do {
                newContent = scanner.nextLine();

                if (!newContent.trim().split(" ")[0].equals("EOF")) {
                    Files.writeString(filePath, newContent + System.lineSeparator() , StandardOpenOption.APPEND);
                }
            } while (!newContent.trim().split(" ")[0].equals("EOF"));

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}