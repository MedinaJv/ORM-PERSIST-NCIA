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
package io.github.guisso.taskmanagement.task;

import io.github.guisso.taskmanagement.repository.Dao;
import io.github.guisso.taskmanagement.repository.DbConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe SpecialTaskDao
 * 
 * A special task creates a one-to-one relationship with Task table
 * reusing your existing structure
 *
 * <code>
 * CREATE TABLE tarefaespecial (
 *  id bigint(20) unsigned NOT NULL,
 *  especial tinyint(1) DEFAULT 0,
 *  PRIMARY KEY (id),
 *  CONSTRAINT tarefaespecial_ibfk_1 
 *      FOREIGN KEY (id) REFERENCES tarefa (id)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=latin1;
 * </code>
 *
 * @author Luis Guisso &lt;luis dot guisso at ifnmg dot edu dot br&gt;
 * @version 0.2, 2024-08-29
 */
public class SpecialTaskDao extends Dao<SpecialTask> {

    public static final String TABLE = "tarefaespecial";

    @Override
    public String getSaveStatment() {
        return "INSERT INTO " + TABLE + " (id, especial) VALUES (?, ?)";
    }

    @Override
    public String getUpdateStatment() {
        return "UPDATE " + TABLE + " SET especial = ? WHERE id = ?";
    }

    @Override
    public String getDeleteStatement() {
        return "DELETE FROM " + TABLE + " WHERE id = ?";
    }

    @Override
    public Long saveOrUpdate(SpecialTask specialTask) {
        Long taskId = new TaskDao().saveOrUpdate(specialTask);

        if (specialTask.getId() == null || specialTask.getId() == 0) {
            specialTask.setId(-taskId);
        } else {
            specialTask.setId(taskId);
        }

        super.saveOrUpdate(specialTask);
        return taskId;
    }

    @Override
    public void composeSaveOrUpdateStatement(PreparedStatement pstmt, SpecialTask specialTask) {
        try {
            if (specialTask.getId() != null && specialTask.getId() < 0) {
                pstmt.setLong(1, -specialTask.getId());
                pstmt.setBoolean(2, specialTask.isSpecial());
            } else {
                pstmt.setBoolean(1, specialTask.isSpecial());
                pstmt.setLong(2, specialTask.getId());
            }
        } catch (SQLException ex) {
            Logger.getLogger(SpecialTaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getFindByIdStatment() {
        return "SELECT t.id, name, email, password, last_access, active, especial"
                + " FROM " + TABLE + " t"
                + " INNER JOIN " + TaskDao.TABLE + " te"
                + " ON t.id = te.id"
                + " WHERE t.id = ?";
    }

    @Override
    public String getFindAllStatment() {
        return "SELECT t.id, name, email, password, last_access, active, especial"
                + " FROM " + TABLE + " t"
                + " INNER JOIN " + TaskDao.TABLE + " te"
                + " ON t.id = te.id"
                + " WHERE excluido = false";
    }

    @Override
    public SpecialTask extractObject(ResultSet resultSet) {
        SpecialTask specialTask = new SpecialTask();

        try {
            specialTask.setId(resultSet.getLong("id"));
            specialTask.setName(resultSet.getString("name"));
            specialTask.setEmail(resultSet.getString("email"));
            specialTask.setPassword(resultSet.getString("password"));
            specialTask.setLastAccess(resultSet.getObject("last_access", LocalDate.class));
            specialTask.setActive(resultSet.getBoolean("active"));
            specialTask.setSpecial(resultSet.getBoolean("especial"));
        } catch (SQLException ex) {
            Logger.getLogger(SpecialTaskDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return specialTask;
    }

    @Override
    public String getMoveToTrashStatement() {
        return "UPDATE " + TABLE + " SET excluido = true WHERE id = ?";
    }

    @Override
    public String getRestoreFromTrashStatement() {
        return "UPDATE " + TABLE + " SET excluido = false WHERE id = ?";
    }

    @Override
    public String getFindAllOnTrashStatement() {
        return "SELECT t.id, name, email, password, last_access, active, especial"
                + " FROM " + TABLE + " t"
                + " INNER JOIN " + TaskDao.TABLE + " te"
                + " ON t.id = te.id"
                + " WHERE excluido = true";
    }

    @Override
    public void moveToTrash(SpecialTask specialTask) {
         try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
            getMoveToTrashStatement())) {
        
        preparedStatement.setLong(1, specialTask.getId());
        System.out.println(">> SQL: " + preparedStatement);
        preparedStatement.executeUpdate();
        
    } catch (Exception ex) {
        System.out.println("Exception: " + ex);
    }
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
    public List<SpecialTask> findAllOnTrash() {
        List<SpecialTask> specialTasks = new ArrayList<>();

    try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
            getFindAllOnTrashStatement())) {
        
        System.out.println(">> SQL: " + preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        
        return extractObjects(resultSet);
        
    } catch (Exception ex) {
        System.out.println("Exception: " + ex);
    }

    return specialTasks;
    }
}
