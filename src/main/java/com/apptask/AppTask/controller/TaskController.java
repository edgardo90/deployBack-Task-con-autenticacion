/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apptask.AppTask.controller;
//
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.CrossOrigin;//configura los coors
import org.springframework.web.bind.annotation.RequestMethod;// sirve para configurar los coors
import org.springframework.web.bind.annotation.GetMapping; // esto lo importo yo
import org.springframework.web.bind.annotation.PathVariable; // esto para que sea por params
import org.springframework.web.bind.annotation.RequestParam; // esto sirve para venir por query
import org.springframework.web.bind.annotation.RestController; // esto lo importo yo
import org.springframework.http.HttpStatus; // le doy el status si esta ok (creo que tambien si esta todo mal)
import org.springframework.http.ResponseEntity; //para enviar datos o un mensaje en formato json
import org.springframework.web.bind.annotation.PostMapping;//ruta para el post
import org.springframework.web.bind.annotation.RequestBody; // esto sirve para traer las variables del post
import org.springframework.web.bind.annotation.ResponseBody; //
import org.springframework.web.bind.annotation.DeleteMapping; // ruta para el deleted
import org.springframework.web.bind.annotation.PutMapping; // ruta para el put
import org.springframework.beans.factory.annotation.Autowired;//// para el service , en este caso ITaskService
import java.util.List; //
import java.util.ArrayList;// esto sirve para crear un array
import java.util.*;
import java.util.stream.*;
//importo lo que cree
import com.apptask.AppTask.model.TasksModel;
import com.apptask.AppTask.model.StatusRuta; // modelo que sirve para enviar un msg en formato json
import com.apptask.AppTask.service.ITaskService;
import com.apptask.AppTask.Security.Dto.NuevoUsuario;
import com.apptask.AppTask.Security.Model.Usuario;
import com.apptask.AppTask.Security.Service.UsuarioService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 *
 * @author edgar
 */
@RestController // para emepezar hacer el ruteo
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}) // configuro los corrs
public class TaskController {

    @Autowired
    private ITaskService interTask;
    @Autowired
    UsuarioService usuarioService;
    
    
    @PreAuthorize("hasRole('USER')") // con esto solo van poder ingresar esta ruta si esta logueado y tiene como rol "USER"
    @GetMapping("/task/all")//traigo todas las tareas
    public List<TasksModel> getTasks() {
        List<TasksModel> task = interTask.getTasks();
        return task;
    }

    @GetMapping("/task/all/{reminder}")
    public List<TasksModel> getReminder(@PathVariable String reminder) { // funcion que traigo las tasks si el reminder es true o false
        List<TasksModel> tasks = interTask.getTasks(); // traigo todas las tareas
        ArrayList<TasksModel> tareas = new ArrayList<>(); // creo una lista de array , en este array voy a guardar las tareas que van a ser true o false
        for (TasksModel el : tasks) { // hago un for que "el" sea una variable del tipo TasksModel
            if ("true".equals(reminder)) { // con esto me fijo si el params "reminder" es true
                if (el.reminder == true) {
                    //System.out.println(el.reminder);
                    tareas.add(el);
                }
            } else if ("false".equals(reminder)) { // es lo mismo que el caso de arriba pero lo contrario
                if (el.reminder == false) {
                    //System.out.println(el.reminder);
                    tareas.add(el);
                }
            }
        }
        return tareas;
    }

    @PostMapping("/task/create")
    public ResponseEntity<Object> createTask(@RequestBody TasksModel task) { // tiene que enviar en formato json sino en el front tira error
        interTask.saveTask(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/task/deleted/{id}")
    public ResponseEntity<Object> deletedTask(@PathVariable Long id) {// elemino la tarea por su id ,como tasksModel esta vinculado con el modelo usuario cuando elemino la tarea tambien se elemina la tarea en usuario
        TasksModel task = interTask.findTask(id); // busco la task por su id y lo guardo en una variable, .findTask(id) es una funcion que cree para buscar por su id
        //System.out.println(task); // esto es como un console.log de javaScript
        if (task == null) { // si task es null
            StatusRuta status = new StatusRuta("No se encontro esa tarea para eleminar"); // creo un msg con el Status model que cree
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST); // envio un msj que no se pudo eleminar
        } else { // caso contrario como task !== null(en javaScript) , elemino la task
            interTask.deleteTask(id);
            StatusRuta status = new StatusRuta("Se elemino la tarea");
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable Long id) { // traigo una tarea por su id
        TasksModel task = interTask.findTask(id);
        if (task == null) {
            StatusRuta status = new StatusRuta("No se encontro esa tarea");
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("task/putreminder/{id}")
    public ResponseEntity<Object> editReminder(@PathVariable Long id) {
        TasksModel task = interTask.findTask(id); // en "TasksModel task" creo una variable "task" de tipo del modelo que cree "TasksModel"
        //System.out.println(task.reminder);// esto es como un console.log() veo que hay en task.reminder
        if (task == null) {
            StatusRuta status = new StatusRuta("no se encontro esa tarea");
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
        }
        if (task.reminder == false) { // si es false
            task.setReminder(true); //con .setReminder() lo cambio a true
        } else {
            task.setReminder(false); // sino hago la viceversa
        }
        interTask.saveTask(task); // lo guardo
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    //esto es un ejemplo
    @GetMapping("/")
    ResponseEntity<Object> traer() {//ejemplo de enviar un mensaje de status en formato json
        //TasksModel task = interTask.findTask(1L);
        //task.setText("www");
        //return new ResponseEntity<>(task , HttpStatus.OK);
        StatusRuta status = new StatusRuta("funciona la ruta");
        if ("funciona la ruta".equals(status.getMsg())) { // con el .equals() me fijo si ese string es igual que en el status.getMsg()
            System.out.println("El if funciona");
        }
        System.out.println(status.getMsg());
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping("/task/user/create/{userName}") // la tarea que creo se vincula y se guarda en el Usuario
    public ResponseEntity<?> createTask(@RequestBody TasksModel task, @PathVariable String userName) {
        Optional<Usuario> user = usuarioService.getByNombreUsuario(userName); // busco el usuario por su username
        interTask.saveTask(task); // guardo la tarea
        //esta es otra forma de agreagar la task que cree a la tabla Usuario , haciendo un Set
        Set<TasksModel> tareas = new HashSet<>(); // creo un Set de tipo TaskModel y la variable se va llamar "tareas"(creo que esto seria un array)
        tareas.add(task);//agrego la task que cree a "tarea" , como "tareas" es un array puedo usar el .add()
        for (TasksModel el : user.get().getTasks()) { // hago un for que la variable va ser "el" de tipo TaskModel y va reitarar sobre user.get().getTasks() que es un array
            tareas.add(el); // cada "el" va ser una task , lo agrego al array "tareas"
        }
        //
        //aca hago una forma para ordenar por su id
//        ArrayList<TasksModel> tars = new ArrayList<>();
//        for(TasksModel el : tareas){
//            tars.add(el);
//        }
//        Collections.sort(tars , new Comparator<TasksModel>() {// para que se ordene tiene "tars" tiene que hacer un array como lo hago arriba
//            @Override
//            public int compare(TasksModel arg0, TasksModel arg1) {
//                return arg0.getId().compareTo(arg1.getId());
//            }
//        });
        //aca termina ejemplo para ordenar
        user.get().setTasks(tareas); //con el setTasks() lo que guardo la "tarea" en el "user" , esto es con el metodo Set
        //user.get().tasks.add(task); // guardo la tarea en el array "tasks"
        usuarioService.save(user.get()); // guardo los cambios de "user"
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

}
