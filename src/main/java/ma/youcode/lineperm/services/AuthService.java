package ma.youcode.lineperm.services;

import ma.youcode.lineperm.dao.modelsDAO.UserDAO;
import ma.youcode.lineperm.models.User;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    public static boolean isAuth = false;
    public static User currentUser = null;

    public void login(String[] userInfo) {
        UserDAO userDAO = new UserDAO();

        if (userDAO.findByUsername(userInfo[0]) != null) {

            if (!BCrypt.checkpw(userInfo[1], userDAO.findByUsername(userInfo[0]).getPassword())) {
                System.err.println("Password is incorect.\n");
                return;
            }

            isAuth = true;
            currentUser = new User(userDAO.findByUsername(userInfo[0]).getId() , userDAO.findByUsername(userInfo[0]).getUsername() , userDAO.findByUsername(userInfo[0]).getPassword());

            System.out.println();
            System.out.println("Bienvenu " + userInfo[0]);
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
        System.out.println();
    }

    public void logout() {
        isAuth = false;
        currentUser = null;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}