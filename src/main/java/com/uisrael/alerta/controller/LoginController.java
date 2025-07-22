package com.uisrael.alerta.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String mostrarLogin(Principal principal) {
    	 if (principal != null) {
    	        // Ya está logueado
    	        return "redirect:/alerta/notificacionesUsuario";
    	    }
        return "formularios/login"; // o la ruta donde tengas tu login.html
    }
}
