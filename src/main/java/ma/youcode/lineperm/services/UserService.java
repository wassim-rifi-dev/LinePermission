package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import ma.youcode.lineperm.constants.FilePaths;
import ma.youcode.lineperm.dao.modelsDAO.UserDAO;
import ma.youcode.lineperm.models.Fichier;
import ma.youcode.lineperm.models.Log;
import ma.youcode.lineperm.models.User;
import ma.youcode.lineperm.models.enums.LogResult;
import ma.youcode.lineperm.models.enums.LogType;

public class UserService {
    public static List<Log> logs = new ArrayList<>();

    public UserService() {
        loadLogs();
    }

    public void loadLogs() {
        try {
            UserDAO userDAO = new UserDAO();
            Path logsFile = Path.of(FilePaths.LOGS_FILE);

            List<String> logsLines = Files.readAllLines(logsFile);

            logsLines.stream()
                        .forEach(line -> {
                            String[] parts = line.split(";" , 6);

                            if (parts.length == 6) {
                                LocalDate date = LocalDate.parse(parts[0]);
                                LocalTime time = LocalTime.parse(parts[1]);
                                String username = parts[2];
                                LogType type = LogType.valueOf(parts[3]);
                                String fileName = parts[4];
                                LogResult result = LogResult.valueOf(parts[5]);

                                User user = userDAO.findByUsername(username);
                                // Fichier file = files.get(fileName);

                                // logs.add(new Log(date, time, user, type, file, result));
                            }
                        });
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}