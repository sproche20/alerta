package com.uisrael.alerta.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uisrael.alerta.model.UsuarioModel;
import com.uisrael.alerta.service.BarrioService;
import com.uisrael.alerta.service.UsuarioService;

@Controller 
public class UsuarioController {
	@Autowired 
	private BarrioService barrioSer;
	@Autowired
	private UsuarioService userSer;
	@Autowired
	private PasswordEncoder passwordEncoder;
	  @GetMapping("/registro")
	    public String mostrarFormularioRegistro(Model model, Principal principal) {
		  if (principal != null) {
		        return "redirect:/alerta/notificacionesUsuario";
		    }
	        model.addAttribute("nuevoUsuario", new UsuarioModel());
	        model.addAttribute("listaBarrios", barrioSer.listar());
	        return "formularios/registroUsuario";
	    }

	    @PostMapping("/registro")
	    public String procesarRegistro(@ModelAttribute("nuevoUsuario") UsuarioModel usuario, Model model) {
	        // Validar si ya hay un presidente en ese barrio
	    	if (usuario.getRol().equals("PRESIDENTE")) {
	    		boolean yaExiste=userSer.existePresidenteEnBarrio(usuario.getBarrioModel().getId());
	    		if (yaExiste) {
	    			model.addAttribute("error", "Ya existe un presidente registrado para ese barrio.");
	                model.addAttribute("listaBarrios", barrioSer.listar());
	                return "formularios/registroUsuario";
				}
	    		
			}
	    	  // ✅ Nuevas validaciones para roles
	        if (usuario.getRol().equals("POLICIA") || usuario.getRol().equals("CIUDADANO")) {
	            usuario.setEnabled(false); // Requiere validación manual
	        } else {
	            usuario.setEnabled(true); // ADMIN o PRESIDENTE por defecto habilitados
	        }
	        // Encriptar contraseña y guardar
	    	usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	        userSer.insertar(usuario);
	        model.addAttribute("mensaje", "Registro exitoso. Espera aprobación si eres policía.");
	        return "redirect:/login";
	    }
	    //usuario del barrio
	    @GetMapping("/usuariosPresidente")
	    @PreAuthorize("hasRole('PRESIDENTE')")
	    public String usuariosDelBarrio(Model model, Principal principal) {
	    	String correo=principal.getName();
	    	UsuarioModel presidente=userSer.buscarPorCorreo(correo).orElse(null);
	    	if (presidente == null) return "redirect:/login";
	    	Long barrioId=presidente.getBarrioModel().getId();
	    	List<UsuarioModel>usuarios=userSer.listarPorBarrio(barrioId);
	    	model.addAttribute("listaUsuarios",usuarios);
	    	return "tablas/usuariosPresidente";
	    	
	    }
	    //Controlador para listar policías no habilitados:------------------------------------------------------------
	    @GetMapping("/admin/policiasPendientes")
	    @PreAuthorize("hasRole('ADMIN')")
	    public String verPoliciasPendientes(Model model) {
	        List<UsuarioModel> policias = userSer.listarPoliciasNoValidados();
	        model.addAttribute("listaPolicias", policias);
	        return "tablas/policiasPendientes";
	    }
	    //habilitar
	    @GetMapping("/admin/habilitarPolicia/{id}")
	    @PreAuthorize("hasRole('ADMIN')")
	    public String habilitarPolicia(@PathVariable Long id,RedirectAttributes redirectAttrs) {
	    	UsuarioModel policia = userSer.buscarPorId(id);
	    	 policia.setEnabled(true);                  
	    	    userSer.insertar(policia); 
	    	    redirectAttrs.addFlashAttribute("mensaje", "✅ Policía habilitado con éxito.");
	        return "redirect:/admin/policiasPendientes";
	    }
	    //usuarios---------------------------------------------------------------------------------
	    @GetMapping("/usuariosPendientes")
	    @PreAuthorize("hasRole('PRESIDENTE')")
	    public String verUsuariosPendientes(Model model, Principal principal) {
	        UsuarioModel presidente = userSer.buscarPorCorreo(principal.getName()).orElse(null);
	        if (presidente == null) return "redirect:/login";

	        Long barrioId = presidente.getBarrioModel().getId();
	        List<UsuarioModel> pendientes = userSer.listarCiudadanosPendientes(barrioId);
	        model.addAttribute("listaPendientes", pendientes);
	        return "tablas/usuariosPendientesBarrio";
	    }
	    
	    @GetMapping("/habilitarUsuario/{id}")
	    @PreAuthorize("hasRole('PRESIDENTE')")
	    public String habilitarUsuario(@PathVariable Long id, RedirectAttributes redirectAttrs) {
	        UsuarioModel user = userSer.buscarPorId(id);
	        user.setEnabled(true);
	        userSer.insertar(user);
	        redirectAttrs.addFlashAttribute("mensaje", "✅ Usuario habilitado correctamente.");
	        return "redirect:/usuariosPendientes";
	    }


	    
	    @GetMapping("/inicio")
	    public String redireccionSegunRol(Principal principal) {
	        UsuarioModel user = userSer.buscarPorCorreo(principal.getName()).orElse(null);
	        if (user == null) return "redirect:/login";

	        switch (user.getRol()) {
	            case "ADMIN": return "redirect:/admin/policiasPendientes";
	            case "PRESIDENTE": return "redirect:/alerta/incidentesBarrio";
	            case "POLICIA": return "redirect:/alerta/rondasPolicia";
	            default: return "redirect:/alerta/incidenteForm"; // ciudadano u otro
	        }
	    }
}
