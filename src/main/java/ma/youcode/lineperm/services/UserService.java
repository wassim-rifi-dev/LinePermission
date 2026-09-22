package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import ma.youcode.lineperm.constants.FilePaths;
import ma.youcode.lineperm.models.Fichier;
import ma.youcode.lineperm.models.Log;
import ma.youcode.lineperm.models.User;
import ma.youcode.lineperm.models.enums.LogResult;
import ma.youcode.lineperm.models.enums.LogType;

public class UserService {
    public static HashMap<String , User> users = new HashMap<>();
    public static HashMap<String , Fichier> files = new HashMap<>();
    public static List<Log> logs = new ArrayList<>();

    public UserService() {
        loadUsers();
        loadFiles();
        loadLogs();
    }

    public void loadUsers() {
        try {
            Path userFile = Path.of(FilePaths.userFile);

            List<String> userLines = Files.readAllLines(userFile);

            for (String line : userLines) {
                String[] parts = line.split(":", 2);

                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];

                    User newUser = new User(username , password);

                    users.put(username, newUser);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void loadFiles() {
        try {
            Path filesFile = Path.of(FilePaths.filesFile);

            List<String> fileLines = Files.readAllLines(filesFile);

            for (String line : fileLines) {
                String[] parts = line.split(" ", 3);

                if (parts.length == 3) {
                    String permission = parts[0];
                    String owner = parts[1];
                    String file = parts[2];

                    Fichier fichierProtege = new Fichier(permission, owner, file);

                    files.put(file, fichierProtege);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void loadLogs() {
        try {
            Path logsFile = Path.of(FilePaths.LOGS_FILE);

            List<String> logsLines = Files.readAllLines(logsFile);

            logsLines.stream()
                        .forEach(line -> {
                            String[] parts = line.split(";" , 6);

                            if (parts.length == 6) {
                                LocalDate date = LocalDate.parse(parts[0]);
                                LocalTime time = LocalTime.parse(parts[1]);
                                String user = parts[2];
                                LogType type = LogType.valueOf(parts[3]);
                                String file = parts[4];
                                LogResult result = LogResult.valueOf(parts[5]);

                                logs.add(new Log(date, time, user, type, file, result));
                            }
                        });
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
