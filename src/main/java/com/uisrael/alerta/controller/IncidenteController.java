package com.uisrael.alerta.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.alerta.model.BarrioModel;
import com.uisrael.alerta.model.IncidenteModel;
import com.uisrael.alerta.model.NotificacionModel;
import com.uisrael.alerta.model.UsuarioModel;
import com.uisrael.alerta.service.BarrioService;
import com.uisrael.alerta.service.IncidenteService;
import com.uisrael.alerta.service.NotificacionService;
import com.uisrael.alerta.service.UsuarioService;

@Controller
@RequestMapping("/alerta")
public class IncidenteController {
	@Autowired
	private IncidenteService incidenteSer;
	@Autowired 
	private BarrioService barrioSer;


	@Autowired
	private NotificacionService notificacionSer;
	@Autowired
	private UsuarioService userSer;
	@GetMapping("/incidenteForm")
	 public String accesoFormulario(Model model) {
	     System.out.println("Accediendo al formulario");
	     model.addAttribute("nuevo", new IncidenteModel());
	     model.addAttribute("listaTipos", List.of(
	    	        "Robo", "Asalto", "Disparos", "Riña callejera",
	    	        "Sospechoso rondando", "Incendio", "Accidente de tránsito", "Emergencia médica", "Otro"
	    	    ));
	     
	    	    return "formularios/incidenteForm";

	 }
	@GetMapping("/incidenteTable")
	 public String accesoTabla(Model model) {
		List<IncidenteModel>resultado=incidenteSer.listar();
		model.addAttribute("listaIncidente", resultado);
	     return "tablas/incidenteTable";
	 }

	//guardar
			@PostMapping("/guardarIncidente")
			public  String guardarIncidente(@ModelAttribute("nuevo") IncidenteModel nuevoIncidente)
			{
				 // Obtener usuario logueado
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				String correo = auth.getName(); // <- así es suficiente
				UsuarioModel usuarioLogueado = userSer.buscarPorCorreo(correo).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

				 // Completar datos automáticamente
			    nuevoIncidente.setUsuarioModel(usuarioLogueado);
			    nuevoIncidente.setBarrioModel(usuarioLogueado.getBarrioModel()); // su propio barrio
			    nuevoIncidente.setDireccion("Automática desde barrio: " + usuarioLogueado.getBarrioModel().getNombre());
			    nuevoIncidente.setFechaHora(LocalDateTime.now());
			    nuevoIncidente.setEstado("PENDIENTE");

			    incidenteSer.insertar(nuevoIncidente);
				
				//Crear notificacion interna
				String mensaje= "🚨 Alerta en el barrio "+nuevoIncidente.getBarrioModel().getNombre()
						+"\nTipo: "+nuevoIncidente.getTipo()
						+"\nDescripción: "+nuevoIncidente.getDescripcion();
				NotificacionModel noti=new NotificacionModel();
				noti.setMensaje(mensaje);
				noti.setFechaEnvio(LocalDateTime.now());
				noti.setIncidenteModel(nuevoIncidente);
				noti.setBarrioModel(nuevoIncidente.getBarrioModel());
				noti.setMedio("INTERNA");
				notificacionSer.insertar(noti);				
				return "redirect:incidenteForm";
			}
			 @GetMapping("/eliminarIncidente/{id}")
			 public String eliminarIncidente(@PathVariable("id") Long id) {
				 incidenteSer.eliminar(id);
			     return "redirect:/alerta/incidenteTable";  // Redirigir a la tabla después de la eliminación
			 }
			 //incidente presidente 
			 @GetMapping("/incidentesPresidente")
			 @PreAuthorize("hasRole('PRESIDENTE')")
			 public String incidentesDeMiBarrio(Model model, Principal principal) {
				 String correo=principal.getName();//obtener el correo del usuario logueado
				 UsuarioModel presidente=userSer.buscarPorCorreo(correo).orElse(null);
				 if (presidente==null) return "redirect:/login";
				 Long barrioId=presidente.getBarrioModel().getId();
				 List<IncidenteModel>incidentes=incidenteSer.listarPorBarrio(barrioId);
				 model.addAttribute("listaIncidente", incidentes);
				 return "tablas/incidentesPresidente"; 
			 }
			 
			 //actualilizar estado presidente
			 @GetMapping("/estadoIncidente/{id}")
			 @PreAuthorize("hasRole('PRESIDENTE')")
			 public String mostrarFormularioEstado(@PathVariable("id") Long id, Model model) {
			     IncidenteModel incidente = incidenteSer.buscarPorId(id);
			     if (incidente == null) return "redirect:/tablas/incidentesPresidente";

			     model.addAttribute("incidente", incidente);
			     return "formularios/cambiarEstadoIncidente";
			 }
			 @PostMapping("/actualizarEstado")
			 @PreAuthorize("hasRole('PRESIDENTE')")
			 public String actualizarEstado(@ModelAttribute("incidente") IncidenteModel incidenteActualizado) {
			     IncidenteModel existente = incidenteSer.buscarPorId(incidenteActualizado.getId());
			     if (existente == null) return "redirect:/tablas/incidentesPresidente";
			     // ✅ Solo si el estado fue cambiado a RESUELTO, se envía una notificación

			     if ("RESUELTO".equalsIgnoreCase(incidenteActualizado.getEstado())) {
			    	    NotificacionModel noti = new NotificacionModel();
			    	    noti.setMensaje("✅ El incidente '" + existente.getTipo() + "' ha sido marcado como RESUELTO.");
			    	    noti.setFechaEnvio(LocalDateTime.now());
			    	    noti.setIncidenteModel(existente);
			    	    noti.setMedio("INTERNA");
			    	    notificacionSer.insertar(noti);
			    	}

			     // Actualizar el estado del incidente
			     existente.setEstado(incidenteActualizado.getEstado());
			     incidenteSer.insertar(existente); // reutilizamos el método guardar
			     return "redirect:/tablas/incidentesPresidente";
			 }
}
