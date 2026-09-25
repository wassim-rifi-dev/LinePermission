package ma.youcode.lineperm.dao.modelsDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.youcode.lineperm.dao.AbstractDAO;
import ma.youcode.lineperm.models.Log;
import ma.youcode.lineperm.models.User;

public class LogDAO extends AbstractDAO<Log> {
    @Override
    public void save(Log log) {
        String sql = "INSERT INTO logs (log_date, log_time, user_id, type, file_id, result) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = AbstractDAO.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            statement.setDate(1, Date.valueOf(log.getDate()));
            statement.setTime(2, Time.valueOf(log.getTime()));
            statement.setLong(3, log.getUser().getId());
            statement.setString(4, log.getType().name());
            statement.setLong(5, log.getFile().getId());
            statement.setString(6, log.getResult().name());

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    log.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    @Override
    public Log findById(long id) {
        return null;
    }

    @Override
    public boolean delete(Log log) {
        return false;
    }

    public long countTotal() {
        String sql = "SELECT COUNT(*) FROM logs";

        try (
                Connection connection = AbstractDAO.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                return result.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return 0;
    }

    public long countRefusedLogTotal() {
        String sql = "SELECT COUNT(*) FROM logs where result = REFUSE";

        try (
                Connection connection = AbstractDAO.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                return result.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return 0;
    }

    public List<User> distinctUsers() {
        String sql = """
                SELECT DISTINCT u.id, u.username, u.password
                FROM users u
                INNER JOIN logs l ON l.user_id = u.id;
                """;

        List<User> users = new ArrayList<>();

        try (
                Connection connection = AbstractDAO.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                users.add(new User(
                        result.getLong("id"),
                        result.getString("username"),
                        result.getString("password")));
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return users;
    }

    public Map<String, Long> logsNumberByUser() {
        String sql = """
                SELECT u.username, COUNT(l.id) AS nombre_logs
                FROM users u
                INNER JOIN logs l ON l.user_id = u.id
                GROUP BY u.id, u.username;
                """;

        Map<String, Long> logsByUser = new HashMap<>();

        try (
                Connection connection = AbstractDAO.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                logsByUser.put(result.getString("username"), result.getLong("nombre_logs"));
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return logsByUser;
    }

    public List<String> topThreeFile() {
        String sql = """
                SELECT f.file_name, COUNT(l.id) AS nombre_logs
                FROM fichiers f
                INNER JOIN logs l ON l.file_id = f.id
                GROUP BY f.id, f.file_name
                ORDER BY nombre_logs DESC
                LIMIT 3;
                """;

        List<String> topThreeFiles = new ArrayList<>();

        try (
                Connection connection = AbstractDAO.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                topThreeFiles.add(result.getString("file_name"));
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return topThreeFiles;
    }

    public long userLogRefused(String user) {
        String sql = """
                SELECT COUNT(l.id) AS nombre_refuse
                FROM logs l
                INNER JOIN users u ON l.user_id = u.id
                WHERE u.username = ? AND l.result = 'REFUSE';
            """;

        try (
            Connection connection = AbstractDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, user);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getLong("nombre_refuse");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
        return 0;
    }
}
