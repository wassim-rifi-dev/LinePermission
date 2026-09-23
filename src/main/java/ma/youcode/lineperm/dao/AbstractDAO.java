package ma.youcode.lineperm.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractDAO<T> implements DAO<T> {
    private static final String DB_URL = "jdbc:sqlite:data/lineperm.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
