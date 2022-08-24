/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apptask.AppTask.Security.Repository;

import com.apptask.AppTask.Security.Model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //mapeamos como repositorio
//la interface  extiende  de JpaRepository(que maneja repositorio JPA)
//en los parametros "<>" deben ir: <clase a persistir , tipo de dato de su ID>
public interface iUsuarioRepository extends JpaRepository<Usuario, Integer>{
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
}
