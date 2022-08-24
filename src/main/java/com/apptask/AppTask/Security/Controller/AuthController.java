/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//aca van estar mis rutas para el usurio y para el login
package com.apptask.AppTask.Security.Controller;

import com.apptask.AppTask.Security.Dto.JwtDto;
import com.apptask.AppTask.Security.Dto.LoginUsuario;
import com.apptask.AppTask.Security.Dto.NuevoUsuario;
import com.apptask.AppTask.Security.Model.Rol;
import com.apptask.AppTask.Security.Model.Usuario;
import com.apptask.AppTask.Security.Enums.RolNombre;
import com.apptask.AppTask.Security.Service.RolService;
import com.apptask.AppTask.Security.Service.UsuarioService;
import com.apptask.AppTask.Security.jwt.JwtProvider;
import com.apptask.AppTask.model.StatusRuta; // modelo que sirve para enviar un msg en formato json
//
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;//configura los coors
import org.springframework.web.bind.annotation.RequestMethod;// sirve para configurar los coors
import org.springframework.web.bind.annotation.GetMapping; // esto lo importo yo , sirve para los gets
import org.springframework.web.bind.annotation.PathVariable; // esto para que sea por params
import org.springframework.web.bind.annotation.RequestParam; // esto sirve para venir por query
import org.springframework.web.bind.annotation.RestController; // esto lo importo yo
import org.springframework.http.HttpStatus; // le doy el status si esta ok (creo que tambien si esta todo mal)
import org.springframework.http.ResponseEntity ; //para enviar datos o un mensaje en formato json
import org.springframework.web.bind.annotation.PostMapping;//ruta para el post
import org.springframework.web.bind.annotation.RequestBody ; // esto sirve para traer las variables del post
import org.springframework.web.bind.annotation.DeleteMapping ; // ruta para el deleted
import org.springframework.web.bind.annotation.PutMapping ; // ruta para el put
import org.springframework.beans.factory.annotation.Autowired ;//// para el service , en este caso ITaskService
import java.util.List; //
import java.util.ArrayList;// esto sirve para crear un array
import java.util.Optional;
import com.apptask.AppTask.model.StatusRuta; // modelo que sirve para enviar un msg en formato json


@RestController
@RequestMapping("/auth") // con esto todas las rutas de abajo empieza con /auth
@CrossOrigin(origins = "*" ,methods = {RequestMethod.GET ,RequestMethod.POST , RequestMethod.PUT ,RequestMethod.DELETE })
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    RolService rolService;
    @Autowired
    JwtProvider jwtProvider;
    
    @PostMapping("/nuevo") // creo un nuevo usuario
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity(new Mensaje("Campos mal puestos o email invalido"),HttpStatus.BAD_REQUEST);
        }
        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())){ // si da true que exite ese usuario va tirar ese error
            return new ResponseEntity(new Mensaje("Ese nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        }
        if(usuarioService.existsByEmail(nuevoUsuario.getEmail())){ // si da true que existe ese email va dar el siguiente error
            return new ResponseEntity(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);
        }
        Usuario usuario = new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), // creo un usario utilizando el dto de NuevoUsuario
            nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword())); // en password... lo que hace encriptar la contraseña
        
        Set<Rol> roles = new HashSet<>(); // creo un nuevo set con modelo Rol variable "roles"
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get()); // por defecto agrego el rol de usuario
        
        if(nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        usuario.setRoles(roles); //agrego el rol o roles
        usuarioService.save(usuario); // guardo el usario creado
        
        return new ResponseEntity(new Mensaje("Usuario guardado"),HttpStatus.CREATED);
    }
    
    @PostMapping("/login")// se logea el usuario
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
           StatusRuta status = new StatusRuta("Campos mal puestos");
           return new ResponseEntity(status , HttpStatus.BAD_REQUEST);
        }
        if(!usuarioService.existsByNombreUsuario(loginUsuario.getNombreUsuario()) ){
            StatusRuta status = new StatusRuta("No se encunetra ese usuario en la base de datos");
            return new ResponseEntity(status , HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        System.out.println(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }
    
    //rutas creadas por mi
    @GetMapping("/user/all") // traigo todos los usuarios
    public List<Usuario> getUsers(){
        List<Usuario> users = usuarioService.getUsers();
        return users;
    }
    
    @GetMapping("/user/{userName}") // traigo un usuario por su username
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName){
        Optional<Usuario> usuario = usuarioService.getByNombreUsuario(userName);
        if(usuario == null){
            StatusRuta status = new StatusRuta("No se encontro el usuario con ese user name");
            return new ResponseEntity<>(status , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(usuario , HttpStatus.OK);
    }
    
    @GetMapping("/userbyid/{id}") // trae un usuari por su id
    public  ResponseEntity<?> getUserById( @PathVariable int  id){ // @PathVariable trae la variable por params
        Usuario usuario = usuarioService.findUser(id);
        if(usuario == null){
            StatusRuta status = new StatusRuta("No se encontro el usuario con ese id");
            return new ResponseEntity<>(status , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(usuario , HttpStatus.OK);
    }
    
    @DeleteMapping("/user/deleted/{id}") // elemino un usuario por su id
    public ResponseEntity<?> userDeleted(@PathVariable int id){
        Usuario usuario = usuarioService.findUser(id);
        if(usuario == null){
            StatusRuta status = new StatusRuta("No se encontro el usuario con ese id para eleminar");
            return new ResponseEntity<>(status , HttpStatus.BAD_REQUEST);
        }
        StatusRuta status = new StatusRuta("Usuario eleminado");
        usuarioService.deletedUser(id);
        return new ResponseEntity<>(status ,HttpStatus.OK);
    }
}
