package ma.youcode.lineperm.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ma.youcode.lineperm.dao.AbstractDAO;

public class DAOService {
    public DAOService() {
        createUserTable();
    }

    public void createUserTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(100) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL
                );
                """;

        try (
            Connection connection = AbstractDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
