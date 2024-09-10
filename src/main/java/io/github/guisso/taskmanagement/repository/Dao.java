/*
 * CC BY-NC-SA 4.0
 *
 * Copyright 2022 Luis Guisso &lt;luis dot guisso at ifnmg dot edu dot br&gt;.
 *
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 *
 * You are free to:
 *   Share - copy and redistribute the material in any medium or format
 *   Adapt - remix, transform, and build upon the material
 *
 * Under the following terms:
 *   Attribution - You must give appropriate credit, provide 
 *   a link to the license, and indicate if changes were made.
 *   You may do so in any reasonable manner, but not in any 
 *   way that suggests the licensor endorses you or your use.
 *   NonCommercial - You may not use the material for commercial purposes.
 *   ShareAlike - If you remix, transform, or build upon the 
 *   material, you must distribute your contributions under 
 *   the same license as the original.
 *   No additional restrictions - You may not apply legal 
 *   terms or technological measures that legally restrict 
 *   others from doing anything the license permits.
 *
 * Notices:
 *   You do not have to comply with the license for elements 
 *   of the material in the public domain or where your use 
 *   is permitted by an applicable exception or limitation.
 *   No warranties are given. The license may not give you 
 *   all of the permissions necessary for your intended use. 
 *   For example, other rights such as publicity, privacy, 
 *   or moral rights may limit how you use the material.
 */
package io.github.guisso.taskmanagement.repository;

import io.github.guisso.taskmanagement.entity.Entity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Dao
 *
 * @param <T> Entity data type
 */
public abstract class Dao<T extends Entity> implements IDao<T> {

    public static final String DB = "gestaotarefas";

    @Override
    public Long saveOrUpdate(T e) {
        Long id = 0L;
        
        if (e.getId() == null || e.getId() <= 0) {
            // Insert a new record
            try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
                    getSaveStatment(), Statement.RETURN_GENERATED_KEYS)) {
                
                composeSaveOrUpdateStatement(preparedStatement, e);
                System.out.println(">> SQL: " + preparedStatement);
                preparedStatement.executeUpdate();
                
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    id = resultSet.getLong(1);
                }
                
            } catch (Exception ex) {
                System.out.println(">> " + ex);
            }
        } else {
            // Update existing record
            try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
                    getUpdateStatment())) {
                
                composeSaveOrUpdateStatement(preparedStatement, e);
                System.out.println(">> SQL: " + preparedStatement);
                preparedStatement.executeUpdate();
                
                id = e.getId();
                
            } catch (Exception ex) {
                System.out.println("Exception: " + ex);
            }
        }

        return id;
    }

    @Override
    public void delete(Long id) {
        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
                getDeleteStatement())) {
            
            preparedStatement.setLong(1, id);
            System.out.println(">> SQL: " + preparedStatement);
            preparedStatement.executeUpdate();
            
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
    }

    @Override
    public T findById(Long id) {
        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
                getFindByIdStatment())) {
            
            preparedStatement.setLong(1, id);
            System.out.println(">> SQL: " + preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return extractObject(resultSet);
            }
            
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }

        return null;
    }

    @Override
    public List<T> findAll() {
        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
                getFindAllStatment())) {
            
            System.out.println(">> SQL: " + preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            return extractObjects(resultSet);
            
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }

        return null;
    }

    @Override
public List<T> extractObjects(ResultSet resultSet) throws SQLException {
    List<T> objects = new ArrayList<>();

    while (resultSet.next()) {
        objects.add(extractObject(resultSet)); // Chama o método abstrato que pode lançar SQLException
    }

    return objects.isEmpty() ? null : objects;
}


    /**
     * Abstract method to be implemented by subclasses to extract a single entity from the ResultSet.
     *
     * @param resultSet ResultSet obtained from the database query.
     * @return Extracted entity.
     * @throws java.sql.SQLException
     */
    
    @Override
    public abstract T extractObject(ResultSet resultSet) throws SQLException;

    /**
     * Abstract method to be implemented by subclasses to get the save SQL statement.
     *
     * @return SQL statement for saving.
     */
    @Override
    public abstract String getSaveStatment();

    /**
     * Abstract method to be implemented by subclasses to get the update SQL statement.
     *
     * @return SQL statement for updating.
     */
    @Override
    public abstract String getUpdateStatment();

    /**
     * Abstract method to be implemented by subclasses to get the delete SQL statement.
     *
     * @return SQL statement for deleting.
     */
    @Override
    public abstract String getDeleteStatement();

    /**
     * Abstract method to be implemented by subclasses to get the find by ID SQL statement.
     *
     * @return SQL statement for finding by ID.
     */
    @Override
    public abstract String getFindByIdStatment();

    /**
     * Abstract method to be implemented by subclasses to get the find all SQL statement.
     *
     * @return SQL statement for finding all records.
     */
    @Override
    public abstract String getFindAllStatment();

    /**
     * Abstract method to be implemented by subclasses to compose the save or update SQL statement.
     *
     * @param preparedStatement PreparedStatement to be composed.
     * @param e Entity object.
     * @throws java.sql.SQLException
     */
    @Override
    public abstract void composeSaveOrUpdateStatement(PreparedStatement preparedStatement, T e) throws SQLException;
}
