package ma.youcode.lineperm.dao.modelsDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ma.youcode.lineperm.dao.AbstractDAO;
import ma.youcode.lineperm.models.User;

public class UserDAO extends AbstractDAO<User> {
    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (
            Connection connection = AbstractDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (
            Connection connection = AbstractDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();

            return new User(result.getInt("id"), result.getString("username"), result.getString("password"));
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return null;
    }

    @Override
    public User findById(long id) {
        return null;
    }

    @Override
    public void delete(User user) {

    }
}
