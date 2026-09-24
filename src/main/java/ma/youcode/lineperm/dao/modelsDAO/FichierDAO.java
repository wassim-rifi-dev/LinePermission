package ma.youcode.lineperm.dao.modelsDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ma.youcode.lineperm.dao.AbstractDAO;
import ma.youcode.lineperm.models.Fichier;
import ma.youcode.lineperm.models.User;

public class FichierDAO extends AbstractDAO<Fichier> {
    @Override
    public void save(Fichier fichier) {
        String sql = "INSERT INTO fichiers (permissions, owner_id, file_name) VALUES (?, ?, ?);";

        try (
            Connection connection = AbstractDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, fichier.getPermissions());
            statement.setLong(2, fichier.getOwner().getId());
            statement.setString(3, fichier.getFileName());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    fichier.setId(id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }

    @Override
    public Fichier findById(long id) {
        String sql = "SELECT * FROM fichiers WHERE id = ?";

        try (
            Connection connection = AbstractDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            UserDAO userDAO = new UserDAO();

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    User user = userDAO.findById(result.getLong("owner_id"));

                    if (user == null) {
                        System.out.println("User not exeste.");
                        return null;
                    }

                    return new Fichier(
                        result.getLong("id"), 
                        result.getString("permissions"), 
                        user, 
                        result.getString("file_name")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean delete(Fichier fichier) {
        String sql = "DELETE FROM fichiers WHERE id = ?";

        try (
            Connection connection = AbstractDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, fichier.getId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return false;
    }

    public List<Fichier> findByOwner(User owner) {
        String sql = "SELECT * FROM fichiers WHERE owner_id = ?";
        List<Fichier> fichiers = new ArrayList<>();

        try (
            Connection connection = AbstractDAO.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            UserDAO userDAO = new UserDAO();

            statement.setLong(1, owner.getId());

            try (ResultSet result = statement.executeQuery()) {
                User o = userDAO.findById(owner.getId());

                if (o == null) {
                    System.out.println("User not exeste.");
                    return fichiers;
                }

                while (result.next()) {
                    fichiers.add(new Fichier(
                        result.getLong("id"),
                        result.getString("permissions"),
                        o,
                        result.getString("file_name")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return fichiers;
    }
}
