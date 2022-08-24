/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
////creo una new Java Interface(esto no es una clase)
package com.apptask.AppTask.service;
//
import java.util.List;
import com.apptask.AppTask.model.TasksModel;

/**
 *
 * @author edgar
 */
public interface ITaskService {
    
    public List<TasksModel> getTasks(); // trae las tareas
    
    public void saveTask(TasksModel task ); // crea la tarea
    
    public void deleteTask(Long id); // elemina la tarea
    
    public TasksModel  findTask(Long id); // busca la tarea por su id
    
    
}
