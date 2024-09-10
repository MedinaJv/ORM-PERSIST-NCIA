package io.github.guisso.taskmanagement.repository;

import io.github.guisso.taskmanagement.entity.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe UserDao
 * 
 * Implementa as operações de banco de dados para a entidade User.
 * 
 */
public class UserDao extends Dao<User> {

    @Override
    public String getSaveStatment() {
        return "INSERT INTO users (name, email, password, last_access, active) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    public String getUpdateStatment() {
        return "UPDATE users SET name = ?, email = ?, password = ?, last_access = ?, active = ? WHERE id = ?";
    }

    @Override
    public String getDeleteStatement() {
        return "DELETE FROM users WHERE id = ?";
    }

    @Override
    public String getFindByIdStatment() {
        return "SELECT id, name, email, password, last_access, active FROM users WHERE id = ?";
    }

    @Override
    public String getFindAllStatment() {
        return "SELECT id, name, email, password, last_access, active FROM users";
    }

    @Override
    public void composeSaveOrUpdateStatement(PreparedStatement preparedStatement, User user) throws SQLException {
        // Validações de tamanho
        if (user.getName().length() > 150) {
            throw new SQLException("Name cannot exceed 150 characters");
        }
        if (user.getEmail().length() > 255) {
            throw new SQLException("Email cannot exceed 255 characters");
        }
        if (user.getPassword().length() != 64) {
            throw new SQLException("Password must be exactly 64 characters long");
        }

        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setObject(4, user.getLastAccess()); // Tratamento para LocalDate
        preparedStatement.setBoolean(5, user.isActive());

        // Se for um update, definir o ID
        if (user.getId() != null && user.getId() > 0) {
            preparedStatement.setLong(6, user.getId());
        }
    }

    @Override
    public User extractObject(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setLastAccess(resultSet.getObject("last_access", LocalDate.class));
        user.setActive(resultSet.getBoolean("active"));

        return user;
    }

    @Override
    public List<User> extractObjects(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            users.add(extractObject(resultSet));
        }

        return users;
    }

    @Override
    public String getMoveToTrashStatement() {
        return "UPDATE users SET deleted = true WHERE id = ?";
    }

    @Override
    public void moveToTrash(User user) {
        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
            getMoveToTrashStatement())) {
        
        preparedStatement.setLong(1, user.getId());
        System.out.println(">> SQL: " + preparedStatement);
        preparedStatement.executeUpdate();
        
    } catch (Exception ex) {
        System.out.println("Exception: " + ex);
    }
    }

    @Override
    public String getRestoreFromTrashStatement() {
        return "UPDATE users SET deleted = false WHERE id = ?";
    }

    @Override
    public void restoreFromTrash(Long id) {
         try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
            getRestoreFromTrashStatement())) {
        
        preparedStatement.setLong(1, id);
        System.out.println(">> SQL: " + preparedStatement);
        preparedStatement.executeUpdate();
        
    } catch (Exception ex) {
        System.out.println("Exception: " + ex);
    }
    }

    @Override
    public String getFindAllOnTrashStatement() {
        return "SELECT id, name, email, password, last_access, active FROM users WHERE deleted = true";
    }

    @Override
    public List<User> findAllOnTrash() {
        List<User> users = new ArrayList<>();

    try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
            getFindAllOnTrashStatement())) {
        
        System.out.println(">> SQL: " + preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        return extractObjects(resultSet);
        
    } catch (Exception ex) {
        System.out.println("Exception: " + ex);
    }

    return users;
    }
}
