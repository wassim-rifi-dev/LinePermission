package ma.youcode.lineperm.services;

import ma.youcode.lineperm.exceptions.InvalidPasswordException;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    public static boolean isAuth = false;

    public void login(String username , String password) {
        try {
            if (UserService.users.containsKey(username)) {

                if (!BCrypt.checkpw(password, UserService.users.get(username))) {
                    throw new InvalidPasswordException("Password is incorect.");
                }

                isAuth = true;
            }
        } catch (InvalidPasswordException e) {
            System.out.println(e.getMessage());
        }
    }
}
