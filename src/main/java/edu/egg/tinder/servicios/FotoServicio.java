/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Foto;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.FotoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Gonza Cozzo
 */
@Service
public class FotoServicio {
    
    @Autowired
    private FotoRepositorio fotoRepositorio;
    
    @Transactional
    public Foto guardar(MultipartFile archivo)throws ErrorServicio{
        if(archivo != null && !archivo.isEmpty()){
            try{
            Foto foto = new Foto();
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setContenido(archivo.getBytes());
            
            return fotoRepositorio.save(foto);
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
        
        return null;
    }
    
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo)throws ErrorServicio{
        if(archivo != null){
            try{
            Foto foto = new Foto();
            
            if(idFoto != null){
                Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
                if(respuesta.isPresent()){
                    foto = respuesta.get();
                }
            }
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setContenido(archivo.getBytes());
            
            return fotoRepositorio.save(foto);
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
        
        return null;
        
    }
}
