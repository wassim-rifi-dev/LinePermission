package ma.youcode.lineperm.dao.modelsDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import ma.youcode.lineperm.dao.AbstractDAO;
import ma.youcode.lineperm.models.Log;

public class LogDAO extends AbstractDAO<Log> {
    @Override
    public void save(Log log) {
        String sql = "INSERT INTO logs (log_date, log_time, user_id, type, file_id, result) VALUES (?, ?, ?, ?, ?, ?)";

        try (
            Connection connection = AbstractDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS);
        ) {
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
}
