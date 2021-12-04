/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.tinder.servicios;

import edu.egg.tinder.entidades.Foto;
import edu.egg.tinder.entidades.Mascota;
import edu.egg.tinder.entidades.Usuario;
import edu.egg.tinder.enumeraciones.Sexo;
import edu.egg.tinder.enumeraciones.Tipo;
import edu.egg.tinder.errores.ErrorServicio;
import edu.egg.tinder.repositorios.MascotaRepositorio;
import edu.egg.tinder.repositorios.UsuarioRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Gonza Cozzo
 */
@Service
public class MascotaServicio {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Autowired
    private MascotaRepositorio mascotaRepositorio;
    
    @Autowired
    private FotoServicio fotoServicio;
    
    @Transactional
    public void agregarMascota(MultipartFile archivo,String idUsuario, String nombre, Sexo sexo, Tipo tipo)throws ErrorServicio{
        
        Usuario usuario = usuarioRepositorio.findById(idUsuario).get();
        
        validar(nombre,sexo);
        
        Mascota mascota = new Mascota();
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
        mascota.setAlta(new Date());
        mascota.setUsuario(usuario);
        mascota.setTipo(tipo);
        
        Foto foto = fotoServicio.guardar(archivo);
        mascota.setFoto(foto);
        
        
        mascotaRepositorio.save(mascota);
    }
    
    @Transactional
    public void modificar(MultipartFile archivo,String idUsuario,String idMascota, String nombre, Sexo sexo, Tipo tipo)throws ErrorServicio{
        
        validar(nombre,sexo);
        
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if(respuesta.isPresent()){
            
            Mascota mascota = respuesta.get();
            if(mascota.getUsuario().getId().equals(idUsuario)){
                mascota.setNombre(nombre);
                mascota.setSexo(sexo);
                
                String idFoto = null;
                if(mascota.getFoto()!=null){
                    
                    idFoto = mascota.getFoto().getId();
                }
                
                Foto foto = fotoServicio.actualizar(idFoto, archivo);
                mascota.setFoto(foto);
                mascota.setTipo(tipo);
                
                mascotaRepositorio.save(mascota);
                
            }else {
                throw new ErrorServicio("¡No tiene los permisos suficientes para realizar esta operacion!");
            }
        }else {
            throw new ErrorServicio("No existe una mascota con el identificador solicitado");
        }
        
    }
    
    @Transactional
    public void eliminar(String idUsuario,String idMascota)throws ErrorServicio{
        
        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if(respuesta.isPresent()){
            
             Mascota mascota = respuesta.get();
            if(mascota.getUsuario().getId().equals(idUsuario)){
                
                mascota.setBaja(new Date());
                
                mascotaRepositorio.save(mascota);
            }else {
                throw new ErrorServicio("¡No tiene los permisos suficientes para realizar esta operacion!");
            }
            
        }else {
            throw new ErrorServicio("No existe una mascota con el identificador solicitado");
        }
          
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public void darAlta(String idUsuario, String idMascota) throws ErrorServicio {

        Optional<Mascota> respuesta = mascotaRepositorio.findById(idMascota);
        if (respuesta.isPresent()) {

            Mascota mascota = respuesta.get();
            
            if(mascota.getUsuario().getId().equals(idUsuario)){
            mascota.setBaja(null);
            mascotaRepositorio.save(mascota);

            }else{
                throw new ErrorServicio("¡No tiene los permisos suficientes para realizar esta operacion!");
            }
        } else {
            throw new ErrorServicio("No existe una mascota con el identificador solicitado");
        }
    }
    
    public void validar(String nombre, Sexo sexo)throws ErrorServicio{
        
        if(nombre == null || nombre.isEmpty()){
            throw new ErrorServicio("El nombre de la mascota no puede ser nulo");
        }
        
        if(sexo == null){
            throw new ErrorServicio("El sexo de la mascota no puede ser nulo");
        }
    }
    
        @Transactional(readOnly=true)
    public Mascota buscarPorId(String id) throws ErrorServicio {

        Optional<Mascota> respuesta = mascotaRepositorio.findById(id);
        if (respuesta.isPresent()){
            
            return respuesta.get();
        } else {
            throw new ErrorServicio("La mascota solicitada no existe");
        }

    }
    
    public List<Mascota> buscarMascotasPorUsuario(String id){
        
        return mascotaRepositorio.buscarMascotasPorUsuario(id);
        
    }
    
    public List<Mascota> buscarMascotasDeBaja(String id){
        
        return mascotaRepositorio.buscarMascotasDeBaja(id);
        
    }
}
