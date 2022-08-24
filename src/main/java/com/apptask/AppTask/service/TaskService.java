/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apptask.AppTask.service;
//
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired ;//
import com.apptask.AppTask.repository.TaskRepository; // traigo mi repository que cree
import com.apptask.AppTask.model.TasksModel; // importo el modelo que cree 
import java.util.List;

/**
 *
 * @author edgar
 */

@Service
public class TaskService implements ITaskService{ // implemento el ITaskService.java que cree
    
    @Autowired
    private TaskRepository tareaRepository;
    
    @Override
    public  List<TasksModel> getTasks(){ // traigo todas las tasks
        List<TasksModel> listTasks =  tareaRepository.findAll();
        return listTasks;
    }
    
    @Override
    public void saveTask(TasksModel task){ // creo la task
        tareaRepository.save(task);
    }
    
    @Override
    public void deleteTask(Long id){ // elemino la task
        tareaRepository.deleteById(id);
    }
    
    @Override
    public TasksModel findTask(Long id){ // busco una tasck por su id
        TasksModel task = tareaRepository.findById(id).orElse(null);
        return task;
    }
    
    
}
