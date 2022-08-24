/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//esta es una class que cree que es mi model
package com.apptask.AppTask.model;
//
import javax.persistence.Entity;
import javax.persistence.Id ;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author edgar
 */
//con todo esto creo mi tabla en la base de datos
@Getter @Setter // esto sirve para usar .get o el .set del model que cree
@Entity
public class TasksModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id ;
    private String text;
    private String day;
    public boolean reminder;
    public String userName;
    
}
