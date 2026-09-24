package ma.youcode.lineperm.dao.modelsDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ma.youcode.lineperm.dao.AbstractDAO;
import ma.youcode.lineperm.models.Fichier;

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
        return null;
    }

    @Override
    public void delete(Fichier fichier) {
        
    }
}
