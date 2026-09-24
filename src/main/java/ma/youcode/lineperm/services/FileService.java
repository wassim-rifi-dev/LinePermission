package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import ma.youcode.lineperm.constants.FilePaths;
import ma.youcode.lineperm.dao.modelsDAO.FichierDAO;
import ma.youcode.lineperm.models.Fichier;
import ma.youcode.lineperm.models.Log;
import ma.youcode.lineperm.models.User;
import ma.youcode.lineperm.models.enums.LogResult;
import ma.youcode.lineperm.models.enums.LogType;

public class FileService {

    private final Scanner scanner;

    public FileService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void touch(String fileName) {
        try {
            FichierDAO fichierDAO = new FichierDAO();

            Path logsFile = Path.of(FilePaths.LOGS_FILE);
            User currentUser = AuthService.currentUser;

            if (fichierDAO.findByFileName(fileName) != null) {
                System.out.println("Ce file est existe.");

                String logWriting = LocalDate.now() + ";" + LocalTime.now().withSecond(0).withNano(0) + ";" + currentUser.getUsername() + ";" + LogType.CREATION + ";" + fileName + ";" + LogResult.REFUSE;
                Files.writeString(logsFile , logWriting + System.lineSeparator(), StandardOpenOption.APPEND);

                Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.CREATION, null, LogResult.REFUSE);
                UserService.logs.add(log);

                return;
            }

            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);

            String logWriting = LocalDate.now() + ";" + LocalTime.now().withSecond(0).withNano(0) + ";" + currentUser.getUsername() + ";" + LogType.CREATION + ";" + fileName + ";" + LogResult.OK;

            Files.createFile(filePath);

            Fichier newFichier = new Fichier("rwd|---", currentUser, fileName);
            fichierDAO.save(newFichier);

            Files.writeString(logsFile , logWriting + System.lineSeparator(), StandardOpenOption.APPEND);

            Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.CREATION, newFichier, LogResult.OK);

            UserService.logs.add(log);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void ls() {
        FichierDAO fichierDAO = new FichierDAO();

        List<Fichier> fichiers = fichierDAO.getAll();

        for (Fichier fichier : fichiers) {
            System.out.println(fichier.getPermissions() + " " + fichier.getOwner().getUsername() + " " + fichier.getFileName());
        }
    }

    public void cat(String fileName) {
        try {
            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);
            Path logsFile = Path.of(FilePaths.LOGS_FILE);
            User currentUser = AuthService.currentUser;

            if (!UserService.files.containsKey(fileName)) {
                System.out.println("Ce file n'existe pas.");
                String logWriting = LocalDate.now() + ";" + LocalTime.now().withSecond(0).withNano(0) + ";" + currentUser.getUsername() + ";" + LogType.LECTURE + ";" + fileName + ";" + LogResult.REFUSE;
                Files.writeString(logsFile , logWriting + System.lineSeparator(), StandardOpenOption.APPEND);

                Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.LECTURE, null, LogResult.REFUSE);
                UserService.logs.add(log);

                return;
            }

            Fichier fichier = UserService.files.get(fileName);
            User fileOwner = fichier.getOwner();
            String filePermission = fichier.getPermissions();

            if (!fileOwner.getUsername().equals(currentUser.getUsername())) {
                String[] permissionPart = filePermission.trim().split("\\|", 2);

                if (!permissionPart[1].contains("r")) {
                    String logWriting = LocalDate.now() + ";" + LocalTime.now().withSecond(0).withNano(0) + ";" + currentUser.getUsername() + ";" + LogType.LECTURE + ";" + fileName + ";" + LogResult.REFUSE;
                    Files.writeString(logsFile, logWriting + System.lineSeparator(), StandardOpenOption.APPEND);

                    Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.LECTURE, fichier, LogResult.REFUSE);
                    UserService.logs.add(log);

                    System.out.println("Vous n'avez pas l'acces.");
                    return;
                }
            }

            String content = Files.readString(filePath);

            String logWriting = LocalDate.now() + ";" + LocalTime.now().withSecond(0).withNano(0) + ";" + currentUser.getUsername() + ";" + LogType.LECTURE + ";" + fileName + ";" + LogResult.OK;
            Files.writeString(logsFile, logWriting + System.lineSeparator(), StandardOpenOption.APPEND);

            Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.LECTURE, fichier, LogResult.OK);
            UserService.logs.add(log);

            System.out.println(content);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void nano(String fileName) {
        try {
            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);
            Path logsFile = Path.of(FilePaths.LOGS_FILE);
            User currentUser = AuthService.currentUser;

            if (!UserService.files.containsKey(fileName)) {
                System.out.println("Ce file n'existe pas.");
                String logWriting = LocalDate.now() + ";" + LocalTime.now().withSecond(0).withNano(0) + ";" + currentUser.getUsername() + ";" + LogType.ECRITURE + ";" + fileName + ";" + LogResult.REFUSE;
                Files.writeString(logsFile , logWriting + System.lineSeparator(), StandardOpenOption.APPEND);

                Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.ECRITURE, null, LogResult.REFUSE);
                UserService.logs.add(log);
                
                return;
            }

            Fichier fichier = UserService.files.get(fileName);
            User fileOwner = fichier.getOwner();
            String filePermission = fichier.getPermissions();

            if (!fileOwner.getUsername().equals(currentUser.getUsername())) {
                String[] permissionPart = filePermission.trim().split("\\|", 2);

                if (!permissionPart[1].contains("w")) {
                    String logWriting = LocalDate.now() + ";" + LocalTime.now().withSecond(0).withNano(0) + ";" + currentUser.getUsername() + ";" + LogType.ECRITURE + ";" + fileName + ";" + LogResult.REFUSE;
                    Files.writeString(logsFile, logWriting + System.lineSeparator(), StandardOpenOption.APPEND);

                    Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.ECRITURE, fichier, LogResult.REFUSE);
                    UserService.logs.add(log);

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
                    Files.writeString(filePath, newContent + System.lineSeparator(), StandardOpenOption.APPEND);
                }
            } while (!newContent.trim().split(" ")[0].equals("EOF"));

            String logWriting = LocalDate.now() + ";" + LocalTime.now().withSecond(0).withNano(0) + ";" + currentUser.getUsername() + ";" + LogType.ECRITURE + ";" + fileName + ";" + LogResult.OK;
            Files.writeString(logsFile, logWriting + System.lineSeparator(), StandardOpenOption.APPEND);

            Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.ECRITURE, fichier, LogResult.OK);
            UserService.logs.add(log);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void chmod(String fileName , String per) {
        try {
            User currentUser = AuthService.currentUser;

            if (!UserService.files.containsKey(fileName)) {
                System.out.println("Ce file n'existe pas.");
                return;
            }

            User fileOwner = UserService.files.get(fileName).getOwner();

            if (!fileOwner.getUsername().equals(currentUser.getUsername())) {
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

    private void addPermision(List<String> fileLines , String fileName , User fileOwner , String per , Path filesFile) throws IOException {
        for (int i = 0 ; i < fileLines.size() ; i++) {
            if (fileLines.get(i).contains(fileName) && fileLines.get(i).contains(fileOwner.getUsername())) {
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

                String newLine = "rwd|" + newOtherPer + " " + fileOwner.getUsername() + " " + fileName;

                Fichier fichierProtege = new Fichier("rwd|" + newOtherPer , fileOwner, fileName);

                UserService.files.put(fileName, fichierProtege);
                fileLines.set(i, newLine);
                Files.write(filesFile, fileLines);
                break;
            }
        }
    }

    private void removePermission(List<String> fileLines , String fileName , User fileOwner , String per , Path filesFile) throws IOException {
        for (int i = 0; i < fileLines.size(); i++) {
            if (fileLines.get(i).contains(fileName) && fileLines.get(i).contains(fileOwner.getUsername())) {
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

                String newLine = "rwd|" + newOtherPer + " " + fileOwner.getUsername() + " " + fileName;

                Fichier fichierProtege = new Fichier("rwd|" + newOtherPer , fileOwner, fileName);

                UserService.files.put(fileName, fichierProtege);
                fileLines.set(i, newLine);
                Files.write(filesFile, fileLines);
                break;
            }
        }
    }
}