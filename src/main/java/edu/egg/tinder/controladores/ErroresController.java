/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.egg.tinder.controladores;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Gonza Cozzo
 */
@Controller
public class ErroresController implements ErrorController {
    
    
    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {
        
        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);
        
        switch (httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe";
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado";
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder al recurso";
                break;
            }
            case 404: {
                errorMsg = "El recurso solicitado no fue encontrado";
                break;
            }
            case 500: {
                errorMsg = "Ocurrio un error interno";
                break;
            }
        }
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        
        Map mapa = httpRequest.getParameterMap();
        for (Object key : mapa.keySet()) {
            
            String[] valores = (String[]) mapa.get(key);
            for (String valor : valores) {
                
                System.out.println(key.toString()+ ": " + valor);
            }
        }
        return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    } 
        
    }
    

