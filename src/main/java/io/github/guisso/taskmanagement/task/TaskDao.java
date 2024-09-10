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
 * Classe TaskDao
 *
 * <code>
 * CREATE TABLE `tarefa` (
 * `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
 * `name` varchar(150) NOT NULL,
 * `email` varchar(255) NOT NULL,
 * `password` char(64) NOT NULL,
 * `last_access` date NOT NULL,
 * `active` tinyint(1) DEFAULT '1',
 * `deleted ` tinyint(1) DEFAULT '0',
 * PRIMARY KEY (`id`),
 * UNIQUE KEY `id` (`id`)
 * ) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1
 * </code>
 *
 * @author Luis Guisso &lt;luis dot guisso at ifnmg dot edu dot br&gt;
 * @version 0.2, 2024-08-29
 */
    public class TaskDao extends Dao<Task> {

        public static final String TABLE = "task";

        @Override
        public String getSaveStatment() {
            return "INSERT INTO " + TABLE
                    + " (name, email, password, last_access, active)"
                    + " VALUES (?, ?, ?, ?, ?)";
        }

        @Override
        public String getUpdateStatment() {
            return "UPDATE " + TABLE
                    + " SET name = ?, email = ?, password = ?, last_access = ?, active = ?"
                    + " WHERE id = ?";
        }

        @Override
        public String getDeleteStatement() {
            return "DELETE FROM " + TABLE + " WHERE id = ?";
        }

        @Override
        public String getFindByIdStatment() {
            return "SELECT id, name, email, password, last_access, active"
                    + " FROM " + TABLE
                    + " WHERE id = ?";
        }

        @Override
        public String getFindAllStatment() {
            return "SELECT id, name, email, password, last_access, active"
                    + " FROM " + TABLE;
        }

        @Override
        public void composeSaveOrUpdateStatement(PreparedStatement pstmt, Task task) {
            try {
                pstmt.setString(1, task.getName());
                pstmt.setString(2, task.getEmail());
                pstmt.setString(3, task.getPassword());
                pstmt.setObject(4, task.getLastAccess(), java.sql.Types.DATE);
                pstmt.setBoolean(5, task.getActive());

                // Se for um update, definir o ID
                if (task.getId() != null && task.getId() > 0) {
                    pstmt.setLong(6, task.getId());
                }

            } catch (SQLException ex) {
                Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public Task extractObject(ResultSet resultSet) {
            Task task = null;
            try {
                task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setName(resultSet.getString("name"));
                task.setEmail(resultSet.getString("email"));
                task.setPassword(resultSet.getString("password"));
                task.setLastAccess(resultSet.getObject("last_access", LocalDate.class));
                task.setActive(resultSet.getBoolean("active"));
            } catch (SQLException ex) {
                Logger.getLogger(TaskDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            return task;
        }

        @Override
        public List<Task> extractObjects(ResultSet resultSet) throws SQLException {
            List<Task> tasks = new ArrayList<>();
            while (resultSet.next()) {
                tasks.add(extractObject(resultSet));
            }
            return tasks;
        }

        @Override
        public String getMoveToTrashStatement() {
            return "UPDATE " + TABLE
                    + " SET deleted = true"
                    + " WHERE id = ?";
        }

        @Override
        public void moveToTrash(Task task) {
            try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
                getMoveToTrashStatement())) {

            preparedStatement.setLong(1, task.getId());
            System.out.println(">> SQL: " + preparedStatement);
            preparedStatement.executeUpdate();

        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }
        }

        @Override
        public String getRestoreFromTrashStatement() {
            return "UPDATE " + TABLE
                    + " SET deleted = false"
                    + " WHERE id = ?";
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
            return "SELECT id, name, email, password, last_access, active"
                    + " FROM " + TABLE
                    + " WHERE deleted = true";
        }

        @Override
        public List<Task> findAllOnTrash() {
            List<Task> tasks = new ArrayList<>();

        try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(
                getFindAllOnTrashStatement())) {

            System.out.println(">> SQL: " + preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            return extractObjects(resultSet);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
        }

        return tasks;
        }

        public List<Task> findByName(String name) {
            final String SQL = "SELECT id, name, email, password, last_access, active"
                    + " FROM " + TABLE
                    + " WHERE name LIKE ?";

            try (PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(SQL)) {
                preparedStatement.setString(1, "%" + name + "%");

                // Mostra a sentença completa
                System.out.println(">> SQL: " + preparedStatement);

                // Realiza a consulta no banco de dados
                ResultSet resultSet = preparedStatement.executeQuery();

                // Retorna os objetos respectivos
                return extractObjects(resultSet);

            } catch (Exception ex) {
                System.out.println("Exception: " + ex);
            }

            return null;
        }

    }