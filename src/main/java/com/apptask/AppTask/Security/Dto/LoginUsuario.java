/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//dto para el login del post login
package com.apptask.AppTask.Security.Dto;

import javax.validation.constraints.NotBlank; // con esto hago que no puede enviar info de strin vacio

/**
 *
 * @author Usuario
 */
public class LoginUsuario {
    @NotBlank // con esto hago que no puede enviar info de strin vacio
    private String nombreUsuario;
    @NotBlank
    private String password;
    
    //Getter & Setter

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
