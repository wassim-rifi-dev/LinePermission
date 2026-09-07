package ma.youcode.lineperm.services;

import java.io.FileNotFoundException;
import java.util.HashMap;

import ma.youcode.lineperm.models.User;

public class UserService {
    HashMap<String , User> users = new HashMap<>();
    
    UserService() {
        loadUsers();
    }

    private void loadUsers() {
        try {

        } catch (FileNotFoundException e) {
            throw new Exception("Error : " + e);
        }
    }
}
