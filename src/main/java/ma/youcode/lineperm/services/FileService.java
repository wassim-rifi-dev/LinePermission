package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

import ma.youcode.lineperm.constants.FilePaths;
import ma.youcode.lineperm.models.FichierProtege;

public class FileService {

    private final Scanner scanner;

    public FileService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void touch(String fileName) {
        try {
            if (UserService.files.containsKey(fileName)) {
                System.out.println("Ce file est existe.");
                return;
            }

            Path filesFile = Path.of(FilePaths.filesFile);
            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);

            String fileWriting = "rwd|--- " + AuthService.currentUser + " " + fileName;

            Files.createFile(filePath);

            Files.writeString(filesFile , fileWriting + System.lineSeparator(), StandardOpenOption.APPEND);

            FichierProtege fichierProtege = new FichierProtege("rwd|---", AuthService.currentUser, fileName);

            UserService.files.put(fileName, fichierProtege);
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

            if (UserService.files.containsKey(fileName)) {
                System.out.println("Ce file est existe.");
                return;
            }

            String fileOwner = UserService.files.get(fileName).getOwner();
            String filePermission = UserService.files.get(fileName).getPermissions();

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
            
            if (UserService.files.containsKey(fileName)) {
                System.out.println("Ce file est existe.");
                return;
            }
            
            String fileOwner = UserService.files.get(fileName).getOwner();
            String filePermission = UserService.files.get(fileName).getPermissions();
            
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

    public void chmod(String fileName , String per) {
        try {
            if (UserService.files.containsKey(fileName)) {
                System.out.println("Ce file est existe.");
                return;
            }

            String fileOwner = UserService.files.get(fileName).getOwner();

            if (!fileOwner.equals(AuthService.currentUser)) {
                System.out.println("Vous n'avez pas l'acces.");
                return;
            }

            Path filesFile = Path.of(FilePaths.filesFile);

            List<String> fileLines = Files.readAllLines(filesFile);

            if (!per.startsWith("-")) {
                addPermision(fileLines, fileName, fileOwner, per, filesFile);
            } else {
                removePermission(fileLines, fileName, fileOwner, per, filesFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void addPermision(List<String> fileLines , String fileName , String fileOwner , String per , Path filesFile) throws IOException {
        for (int i = 0 ; i < fileLines.size() ; i++) {
            if (fileLines.get(i).contains(fileName) && fileLines.get(i).contains(fileOwner)) {
                String[] parts = fileLines.get(i).trim().split(" ");

                String[] perPart = parts[0].trim().split("\\|");

                String newOtherPer = perPart[1];

                for (char permission : per.toCharArray()) {
                    if (permission == 'r') {
                        newOtherPer = "r" + newOtherPer.substring(1);
                    } else if (permission == 'w') {
                        newOtherPer = "rw" + newOtherPer.substring(2);
                    } else if (permission == 'd') {
                        newOtherPer = "rwd";
                    }
                }

                String newLine = "rwd|" + newOtherPer + " " + fileOwner + " " + fileName;

                FichierProtege fichierProtege = new FichierProtege("rwd|" + newOtherPer , fileOwner, fileName);

                UserService.files.put(fileName, fichierProtege);
                fileLines.set(i, newLine);
                Files.write(filesFile, fileLines);
                break;
            }
        }
    }

    private void removePermission(List<String> fileLines , String fileName , String fileOwner , String per , Path filesFile) throws IOException {
        for (int i = 0; i < fileLines.size(); i++) {
            if (fileLines.get(i).contains(fileName) && fileLines.get(i).contains(fileOwner)) {
                String[] parts = fileLines.get(i).trim().split(" ");

                String[] perPart = parts[0].trim().split("\\|");

                String newOtherPer = perPart[1];

                for (char permission : per.toCharArray()) {
                    if (permission == 'r') {
                        newOtherPer = "---";
                    } else if (permission == 'w') {
                        newOtherPer = newOtherPer.substring(0 , 1)  + "--";
                    } else if (permission == 'd') {
                        newOtherPer = newOtherPer.substring(0 , 2) + "-";
                    }
                }

                String newLine = "rwd|" + newOtherPer + " " + fileOwner + " " + fileName;

                FichierProtege fichierProtege = new FichierProtege("rwd|" + newOtherPer , fileOwner, fileName);

                UserService.files.put(fileName, fichierProtege);
                fileLines.set(i, newLine);
                Files.write(filesFile, fileLines);
                break;
            }
        }
    }
}