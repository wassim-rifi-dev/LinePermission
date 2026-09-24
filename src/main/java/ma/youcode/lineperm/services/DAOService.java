package ma.youcode.lineperm.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ma.youcode.lineperm.dao.AbstractDAO;

public class DAOService {
    public DAOService() {
        createUserTable();
        createFichierTable();
        createLogTable();
    }

    public void createUserTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY,
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

    public void createFichierTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS fichiers (
                    id INTEGER PRIMARY KEY,
                    permissions VARCHAR(20) NOT NULL,
                    owner_id INTEGER NOT NULL,
                    file_name VARCHAR(255) NOT NULL,

                    CONSTRAINT fk_fichier_owner
                        FOREIGN KEY (owner_id)
                        REFERENCES users(id)
                        ON DELETE CASCADE
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

    public void createLogTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS logs (
                    id INTEGER PRIMARY KEY,
                    log_date DATE NOT NULL,
                    log_time TIME NOT NULL,
                    user_id INTEGER,
                    type VARCHAR(20) NOT NULL,
                    file_id INTEGER,
                    result VARCHAR(20) NOT NULL,

                    CONSTRAINT fk_log_user
                        FOREIGN KEY (user_id)
                        REFERENCES users(id)
                        ON DELETE SET NULL,

                    CONSTRAINT fk_log_file
                        FOREIGN KEY (file_id)
                        REFERENCES fichiers(id)
                        ON DELETE SET NULL
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
