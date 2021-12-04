/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.tinder.repositorios;

import edu.egg.tinder.entidades.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Gonza Cozzo
 */
@Repository
public interface ZonaRepositorio extends JpaRepository<Zona, String>{
     
}
