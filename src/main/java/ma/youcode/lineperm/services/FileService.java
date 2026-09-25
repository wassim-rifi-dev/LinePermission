package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import ma.youcode.lineperm.constants.FilePaths;
import ma.youcode.lineperm.dao.modelsDAO.FichierDAO;
import ma.youcode.lineperm.dao.modelsDAO.LogDAO;
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
            LogDAO logDAO = new LogDAO();

            Fichier fichier = fichierDAO.findByFileName(fileName);

            User currentUser = AuthService.currentUser;

            if (fichier != null) {
                System.out.println("Ce file est existe.");

                Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.CREATION, fichier, LogResult.REFUSE);
                logDAO.save(log);

                return;
            }

            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);

            Files.createFile(filePath);

            Fichier newFichier = new Fichier("rwd|---", currentUser, fileName);
            fichierDAO.save(newFichier);

            Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.CREATION, newFichier, LogResult.OK);
            logDAO.save(log);
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
            FichierDAO fichierDAO = new FichierDAO();
            LogDAO logDAO = new LogDAO();

            Fichier fichier = fichierDAO.findByFileName(fileName);

            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);
            User currentUser = AuthService.currentUser;

            if (fichier == null) {
                System.out.println("Ce file n'existe pas.");

                Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.CREATION, fichier, LogResult.REFUSE);
                logDAO.save(log);

                return;
            }

            if (!fichier.getOwner().getUsername().equals(currentUser.getUsername())) {
                String[] permissionPart = fichier.getPermissions().trim().split("\\|", 2);

                if (!permissionPart[1].contains("r")) {
                    Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.LECTURE, fichier, LogResult.REFUSE);
                    logDAO.save(log);

                    System.out.println("Vous n'avez pas l'acces.");
                    return;
                }
            }

            String content = Files.readString(filePath);

            Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.LECTURE, fichier, LogResult.OK);
            logDAO.save(log);

            System.out.println(content);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void nano(String fileName) {
        try {
            FichierDAO fichierDAO = new FichierDAO();
            LogDAO logDAO = new LogDAO();

            Fichier fichier = fichierDAO.findByFileName(fileName);

            Path filePath = Path.of(FilePaths.mainFilesDiractories + fileName);
            User currentUser = AuthService.currentUser;

            if (fichier == null) {
                System.out.println("Ce file n'existe pas.");

                Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.ECRITURE, fichier, LogResult.REFUSE);
                logDAO.save(log);

                return;
            }

            if (!fichier.getOwner().getUsername().equals(currentUser.getUsername())) {
                String[] permissionPart = fichier.getPermissions().trim().split("\\|", 2);

                if (!permissionPart[1].contains("w")) {
                    Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.ECRITURE, fichier, LogResult.REFUSE);
                    logDAO.save(log);

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

            Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.ECRITURE, fichier, LogResult.OK);
            logDAO.save(log);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void chmod(String fileName , String per) {
        try {
            FichierDAO fichierDAO = new FichierDAO();
            LogDAO logDAO = new LogDAO();

            Fichier fichier = fichierDAO.findByFileName(fileName);

            User currentUser = AuthService.currentUser;

            if (fichier == null) {
                System.out.println("Ce file n'existe pas.");
                return;
            }

            if (!fichier.getOwner().getUsername().equals(currentUser.getUsername())) {
                System.out.println("Vous n'avez pas l'acces.");
                return;
            }

            if (!per.startsWith("-")) {
                addPermision(fichier , per);
            } else {
                removePermission(fichier , per);
            }

            Log log = new Log(LocalDate.now(), LocalTime.now().withSecond(0).withNano(0), currentUser, LogType.CHANGE_PERMISSION, fichier, LogResult.OK);
            logDAO.save(log);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void addPermision(Fichier fichier , String newPermission) throws SQLException {
        FichierDAO fichierDAO = new FichierDAO();

        String[] perPart = fichier.getPermissions().trim().split("\\|");
        String newOtherPer = perPart[1];

        for (char permission : newPermission.toCharArray()) {
            if (permission == 'r') {
                newOtherPer = "r" + newOtherPer.substring(1);
            } else if (permission == 'w') {
                newOtherPer = "rw" + newOtherPer.substring(2);
            } else if (permission == 'd') {
                newOtherPer = "rwd";
            }
        }

        newPermission = "rwd|" + newOtherPer;

        fichierDAO.updatePermissions(fichier.getId() , newPermission);
    }

    private void removePermission(Fichier fichier , String newPermission) throws SQLException {
        FichierDAO fichierDAO = new FichierDAO();

        String[] perPart = fichier.getPermissions().trim().split("\\|");
        String newOtherPer = perPart[1];

        for (char permission : newPermission.toCharArray()) {
            if (permission == 'r') {
                newOtherPer = "---";
            } else if (permission == 'w') {
                newOtherPer = newOtherPer.substring(0 , 1)  + "--";
            } else if (permission == 'd') {
                newOtherPer = newOtherPer.substring(0 , 2) + "-";
            }
        }

        newPermission = "rwd|" + newOtherPer;

        fichierDAO.updatePermissions(fichier.getId() , newPermission);
    }
}