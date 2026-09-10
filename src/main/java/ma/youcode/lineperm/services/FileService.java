package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
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

            Scanner scanner = new Scanner(System.in);
            String newContent;

            do {
                newContent = scanner.nextLine();

                if (!newContent.trim().split(" ")[0].equals("EOF")) {
                    Files.writeString(filePath, newContent + System.lineSeparator() , StandardOpenOption.APPEND);
                }
            } while (!newContent.trim().split(" ")[0].equals("EOF"));

            scanner.close();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void chmod(String fileName , String per) {
        try {
            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);

            if (!Files.exists(filePath)) {
                System.out.println("Aucune file avec se nom.");
            }

            String fileOwner = UserService.files.get(fileName)[0];

            if (!fileOwner.equals(AuthService.currentUser)) {
                System.out.println("Vous n'avez pas l'acces.");
                return;
            }

            Path filesFile = Path.of(FilePaths.filesFile);

            List<String> fileLines = Files.readAllLines(filesFile);

            for (int i = 0 ; i < fileLines.size() ; i++) {
                if (fileLines.get(i).contains(fileName) && fileLines.get(i).contains(fileOwner)) {
                    String[] parts = fileLines.get(i).trim().split(" ");

                    String[] perPart = parts[0].trim().split("\\|");

                    String newOtherPer = perPart[1];

                    for (char permission : per.toCharArray()) {
                        if (permission == 'r') {
                            newOtherPer = "r" + newOtherPer.substring(1);
                        } else if (permission == 'w') {
                            newOtherPer = newOtherPer.substring(0 , 1)  + "w" + newOtherPer.substring(2);
                        } else if (permission == 'd') {
                            newOtherPer = newOtherPer.substring(0 , 2) + "d";
                        }
                    }

                    String newLine = "rwd|" + newOtherPer + " " + fileOwner + " " + fileName;

                    UserService.files.put(fileName, new String[]{fileOwner , "rwd|" + newOtherPer});
                    fileLines.set(i, newLine);
                    Files.write(filesFile, fileLines);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}