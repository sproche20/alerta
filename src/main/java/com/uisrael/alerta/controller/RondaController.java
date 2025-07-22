package com.uisrael.alerta.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uisrael.alerta.model.IncidenteModel;
import com.uisrael.alerta.model.NotificacionModel;
import com.uisrael.alerta.model.RondaModel;
import com.uisrael.alerta.model.UsuarioModel;
import com.uisrael.alerta.service.BarrioService;
import com.uisrael.alerta.service.NotificacionService;
import com.uisrael.alerta.service.RondaService;
import com.uisrael.alerta.service.UsuarioService;

@Controller
@RequestMapping("/alerta")
public class RondaController {
	@Autowired
	private BarrioService barrioSer;
	@Autowired
	private UsuarioService usuarioSer;
	@Autowired
	private NotificacionService notificacionSer;
	@Autowired
	private RondaService rondaSer;
	//registrar la ronda
	@GetMapping("/formRonda")
	@PreAuthorize("hasRole('POLICIA')")
	public String mostrarFormularioRonda(Model model, Principal principal) {
		UsuarioModel policia=usuarioSer.buscarPorCorreo(principal.getName()).orElse(null);
		if (policia==null||!policia.isEnabled()) {
	        return "redirect:/login";
		}
	    RondaModel ronda = new RondaModel();
	    ronda.setPolicia(policia);// se asigna automáticamente
	    model.addAttribute("ronda", ronda);
	    model.addAttribute("listaBarrios", barrioSer.listar()); // por si puede elegir otro barrio
	    return "formularios/formRonda";
	    

	}
	//procesar ronda
	@PostMapping("/guardarRonda")
	@PreAuthorize("hasRole('POLICIA')")
	public String guardarRonda(@ModelAttribute("ronda") RondaModel ronda, Principal principal, Model model) {
		 UsuarioModel policia = usuarioSer.buscarPorCorreo(principal.getName()).orElse(null);
		    if (policia == null || !policia.isEnabled()) {
		        return "redirect:/login";
		    }
		    ronda.setPolicia(policia); // se asegura que sea el que está logueado
		    
		    // Validar si ya existe una ronda para ese barrio en esa fecha y hora
		    boolean yaExiste = rondaSer.existeRondaEnFechaYBarrio(ronda.getFechaHora(), ronda.getBarrioModel().getId());
		    if (yaExiste) {
		        model.addAttribute("error", "Ya existe una ronda programada en ese barrio a esa hora.");
		        model.addAttribute("listaBarrios", barrioSer.listar());
		        return "formularios/formRonda";
		    }
		    rondaSer.insertar(ronda);
		    // 🔔 Crear notificación
		    NotificacionModel notificacion = new NotificacionModel();
		    notificacion.setMensaje("La policía realizará una ronda en el barrio " + ronda.getBarrioModel().getNombre() +
		            " el día " + ronda.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ".");
		    notificacion.setFechaEnvio(LocalDateTime.now());
		    notificacion.setMedio("APP");

		    // Asocia directamente el barrio
		    notificacion.setBarrioModel(ronda.getBarrioModel());

		    // No necesitas asociar un incidente aquí
		    notificacion.setIncidenteModel(null);

		    notificacionSer.insertar(notificacion);

		    return "redirect:/alerta/rondasPolicia";
	}
	//listar sus propias rondas la policia
	@GetMapping("/rondasPolicia")
	@PreAuthorize("hasRole('POLICIA')")
	public String listarRondasDelPolicia(Model model, Principal principal) {
	    UsuarioModel policia = usuarioSer.buscarPorCorreo(principal.getName()).orElse(null);
	    if (policia == null) return "redirect:/login";
	    
	    List<RondaModel> rondas = rondaSer.listar().stream()
	        .filter(r -> r.getPolicia().getId().equals(policia.getId()))
	        .toList();
	    model.addAttribute("listaRondas", rondas);
	    return "tablas/rondasPolicia";
	}
	@GetMapping("/eliminarRonda/{id}")
	@PreAuthorize("hasRole('POLICIA')")
	public String eliminarRonda(@PathVariable("id") Long id, Principal principal, RedirectAttributes redirectAttrs) {
	    UsuarioModel policia = usuarioSer.buscarPorCorreo(principal.getName()).orElse(null);
	    RondaModel ronda = rondaSer.buscarPorId(id);

	    // Validar que el policía solo pueda eliminar sus propias rondas
	    if (ronda != null && ronda.getPolicia().getId().equals(policia.getId())) {
	        rondaSer.eliminar(id);
	        redirectAttrs.addFlashAttribute("mensaje", "✅ Ronda eliminada correctamente.");
	    } else {
	        redirectAttrs.addFlashAttribute("error", "❌ No puedes eliminar una ronda que no te pertenece.");
	    }

	    return "redirect:/alerta/rondasPolicia";
	}

}
