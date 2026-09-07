package ma.youcode.lineperm.models;

import org.mindrot.jbcrypt.BCrypt;

public class User {
    User(String username , String password) {
        setUsername(username);
        setPassword(password);
    }

    private String username;

    private String password;

    public String getUsername() {
        return  this.username;
    }

    public String getPassword() {
        return  this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
