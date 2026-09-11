package ma.youcode.lineperm.services;

import ma.youcode.lineperm.constants.FilePaths;
import ma.youcode.lineperm.exceptions.InvalidPasswordException;
import ma.youcode.lineperm.exceptions.UserAlreadyExisteException;
import ma.youcode.lineperm.models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    public static boolean isAuth = false;
    public static String currentUser = null;

    public void login(String username , String password) throws RuntimeException {
        if (UserService.users.containsKey(username)) {

            if (!BCrypt.checkpw(password, UserService.users.get(username).getPassword())) {
                System.err.println("Password is incorect.\n");
                return;
            }

            isAuth = true;
            currentUser = username;

            System.out.println("");
        } else {
            System.out.println("Username not exeste.");
        }
    }

    public void singUp(String username , String password) throws RuntimeException {
        if (UserService.users.containsKey(username)) {
            System.err.println("Ce username est existe.");
            return;
        }

        try {
            Path userfile = Path.of(FilePaths.userFile);

            String hashedPassword = hashPassword(password);

            String userWriting = username + ":" + hashedPassword;

            User newUser = new User(username, hashedPassword);

            UserService.users.put(username, newUser);

            Files.writeString(userfile, userWriting + System.lineSeparator() , StandardOpenOption.APPEND);

            isAuth = true;
            currentUser = username;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void logout() {
        isAuth = false;
        currentUser = null;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
