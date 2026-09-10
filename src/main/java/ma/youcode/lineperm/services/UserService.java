package ma.youcode.lineperm.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import ma.youcode.lineperm.constants.FilePaths;
import ma.youcode.lineperm.models.FichierProtege;
import ma.youcode.lineperm.models.User;

public class UserService {
    public static HashMap<String , User> users = new HashMap<>();
    public static HashMap<String , FichierProtege> files = new HashMap<>();

    public UserService() {
        loadUsers();
        loadFiles();
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

                    FichierProtege fichierProtege = new FichierProtege(permission, owner, file);

                    files.put(file, fichierProtege);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
