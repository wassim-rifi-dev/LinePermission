package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import ma.youcode.lineperm.constants.FilePaths;

public class UserService {
    public static HashMap<String , String> users = new HashMap<>();

    public UserService() {
        loadUsers();
    }

    public void loadUsers() {
        try {
            Path userfile = Path.of(FilePaths.userFile);

            List<String> lines = Files.readAllLines(userfile);

            for (String line : lines) {
                String[] parts = line.split(":", 2);

                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];

                    users.put(username, password);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
