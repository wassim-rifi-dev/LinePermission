package ma.youcode.lineperm.services;

import ma.youcode.lineperm.dao.modelsDAO.UserDAO;
import ma.youcode.lineperm.models.User;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    public static boolean isAuth = false;
    public static User currentUser = null;

    public void login(String username , String password) throws RuntimeException {
        if (UserService.users.containsKey(username)) {

            if (!BCrypt.checkpw(password, UserService.users.get(username).getPassword())) {
                System.err.println("Password is incorect.\n");
                return;
            }

            isAuth = true;
            currentUser = UserService.users.get(username);

            System.out.println("");
        } else {
            System.out.println("Username not exeste.");
        }
    }

    public void singUp(String[] userInfo)  {
        UserDAO userDAO = new UserDAO();

        if (userDAO.findByUsername(userInfo[0]) != null) {
            System.err.println("Ce username est deja existe.");
            return;
        }

        User newUser = new User(userInfo[0], hashPassword(userInfo[1]));

        userDAO.save(newUser);

        isAuth = true;
        currentUser = newUser;

        System.out.println("User creer en success.");
    }

    public void logout() {
        isAuth = false;
        currentUser = null;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}