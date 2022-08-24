/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apptask.AppTask.Security.Service;

import com.apptask.AppTask.Security.Model.Usuario; // este es el modelo Usuario que cree
import com.apptask.AppTask.Security.Repository.iUsuarioRepository; // traigo mi repository que cree
//
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;  

@Service
@Transactional // evita errores que se cargue en la base de datos(eso creo)
public class UsuarioService {
    @Autowired
    iUsuarioRepository iusuarioRepository; // el repository que cree lo implemento aca
    
    public Optional<Usuario> getByNombreUsuario(String nombreUsuario){ // trae un usuario por su username
        return iusuarioRepository.findByNombreUsuario(nombreUsuario);
    }
    
    public boolean existsByNombreUsuario(String nombreUsuario){ // me devuelve true si existe ese nombre
        return iusuarioRepository.existsByNombreUsuario(nombreUsuario);
    }
    
    public boolean existsByEmail(String email){ // me devuelve true si existe ese email
        return iusuarioRepository.existsByEmail(email);
    }
    
    public void save(Usuario usuario){ // crea un usuario en la base de datos
        iusuarioRepository.save(usuario);
    }
    
    //funciones  que cree por mi cuenta(funciones echas por mi)
    public List<Usuario> getUsers(){ // me trae todos los usuarios
        List<Usuario> users = iusuarioRepository.findAll();
        return users;
    }
    
    public  void deletedUser(int id){ // elemino un usuario por su id
        iusuarioRepository.deleteById(id);
    }
    
    public Usuario  findUser(int id){
        Usuario usuario = iusuarioRepository.findById(id).orElse(null);
        return usuario;
    }
    
    
}
