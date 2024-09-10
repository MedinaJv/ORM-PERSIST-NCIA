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
package io.github.guisso.taskmanagement;

import io.github.guisso.taskmanagement.task.Task;
import io.github.guisso.taskmanagement.task.TaskDao;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Luis Guisso
 */
public class TaskManagement {
    
    public static void main(String[] args) {

        // Criação de uma nova tarefa (Task)
        Task novaTarefa = new Task(
            null, // ID será gerado automaticamente
            "João Silva", // Nome
            "joao.silva@example.com", // Email
            "1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef", // Senha (64 caracteres)
            LocalDate.now(), // Último acesso
            true // Ativo
        );

        // Salvando a nova tarefa no banco de dados
        TaskDao taskDao = new TaskDao();
        Long id = taskDao.saveOrUpdate(novaTarefa);
        novaTarefa.setId(id); // Definir o ID gerado automaticamente

        // Atualizando a tarefa
        novaTarefa.setName("João Silva Atualizado");
        taskDao.saveOrUpdate(novaTarefa);

        // Recuperando a tarefa por ID
        Task tarefaRecuperada = taskDao.findById(id);
        System.out.println("Tarefa recuperada: " + tarefaRecuperada);

        // Recuperando todas as tarefas
        List<Task> todasAsTarefas = taskDao.findAll();
        System.out.println("Todas as tarefas: " + todasAsTarefas);

        // Exemplo de exclusão de uma tarefa
        taskDao.delete(id);
        System.out.println("Tarefa excluída com sucesso.");
    }
}
