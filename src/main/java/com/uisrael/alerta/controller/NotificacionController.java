package com.uisrael.alerta.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uisrael.alerta.DTO.NotificacionDTO;
import com.uisrael.alerta.model.IncidenteModel;
import com.uisrael.alerta.model.NotificacionModel;
import com.uisrael.alerta.model.UsuarioModel;
import com.uisrael.alerta.service.IncidenteService;
import com.uisrael.alerta.service.NotificacionService;
import com.uisrael.alerta.service.UsuarioService;

@Controller
@RequestMapping("/alerta")
public class NotificacionController {
@Autowired
private NotificacionService notificacionSer;
@Autowired
private UsuarioService userSer;
@Autowired
private IncidenteService incidenteSer;
// 📌 Vista general sin restricción 
@GetMapping("/notificacionesTable")
@PreAuthorize("hasAnyRole('POLICIA', 'PRESIDENTE', 'ADMIN')")
public String listarNotificaciones(Model model) {
    List<NotificacionModel> lista = notificacionSer.listar();
    model.addAttribute("listaNotificaciones", lista);
    return "tablas/notificacionesTable";
}
//👤 Vista filtrada para ciudadanos (sólo su barrio + incidentes recientes)
@GetMapping("/notificacionesUsuario")
@PreAuthorize("hasRole('CIUDADANO')")
public String verNotificacionesUsuario(Model model, Principal principal) {
    UsuarioModel usuario = userSer.buscarPorCorreo(principal.getName()).orElse(null);
    if (usuario == null) return "redirect:/login";

    Long barrioId = usuario.getBarrioModel().getId();

    List<NotificacionModel> notificaciones = notificacionSer.buscarGeneralesYPorBarrio(barrioId);

    model.addAttribute("listaNotificaciones", notificaciones);
    return "tablas/notificacionesTable";
}

// 🔔 Simular una notificación para el panel del navegador (PC/Móvil)
@GetMapping("/simularNoti")
public String simularNotificacion(Model model, Principal principal) {
    UsuarioModel user = userSer.buscarPorCorreo(principal.getName()).orElse(null);
    if (user != null) {
        List<NotificacionModel> notis = notificacionSer.buscarGeneralesYPorBarrio(user.getBarrioModel().getId());
        // Pasamos solo el mensaje, no toda la lista
        String mensaje = notis.isEmpty() ? "No hay nuevas notificaciones" : notis.get(0).getMensaje();
        model.addAttribute("mensajeNoti", mensaje);
    }
    return "notificaciones/simularNoti";
}

@GetMapping("/ultimaNotificacion")
@ResponseBody
public NotificacionDTO obtenerUltimaNotificacion(Principal principal) {
    UsuarioModel usuario = userSer.buscarPorCorreo(principal.getName()).orElse(null);
    if (usuario == null) return null;

    Long barrioId = usuario.getBarrioModel().getId();
    List<NotificacionModel> lista = notificacionSer.buscarGeneralesYPorBarrio(barrioId);

    if (lista.isEmpty()) return null;

    NotificacionModel ultima = lista.get(0);
    return new NotificacionDTO(ultima.getId(), ultima.getMensaje());
}

//Eliminar notificación (opcional)
@GetMapping("/eliminarNotificacion/{id}")
public String eliminarNotificacion(@PathVariable("id") Long id) {
    notificacionSer.eliminar(id);
    return "redirect:/alerta/notificacionesTable";
}
//notificacion presidente barrial.
@GetMapping("/enviarNotificacionBarrio")
@PreAuthorize("hasRole('PRESIDENTE')")
public String mostrarFormulario(Model model) {
    model.addAttribute("nuevaNotificacion", new NotificacionModel());
    return "formularios/notificacionBarrio";
}
@PostMapping("/enviarNotificacionBarrio")
@PreAuthorize("hasRole('PRESIDENTE')")
public String procesarNotificacion(@ModelAttribute("nuevaNotificacion") NotificacionModel noti, Principal principal) {
	String correo = principal.getName();
    UsuarioModel presidente = userSer.buscarPorCorreo(correo).orElse(null);
    if (presidente == null) return "redirect:/login";

    noti.setFechaEnvio(LocalDateTime.now());
    noti.setIncidenteModel(null); // No está relacionada con un incidente
    noti.setMedio("INTERNA"); // Se puede filtrar luego por tipo
    noti.setBarrioModel(presidente.getBarrioModel()); // 👈 Esto asegura que solo los vecinos lo vean
    notificacionSer.insertar(noti);

    return "redirect:/alerta/notificacionesTable";
	}
}
