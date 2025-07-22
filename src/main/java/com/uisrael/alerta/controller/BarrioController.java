package com.uisrael.alerta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uisrael.alerta.model.BarrioModel;
import com.uisrael.alerta.service.BarrioService;
@Controller 
@RequestMapping("/alerta")
public class BarrioController {
	@Autowired 
	private BarrioService barrioSer;

	@GetMapping("/barrioForm")
	 public String accesoFormulario(Model model) {
	     System.out.println("Accediendo al formulario de barrio");
	     model.addAttribute("nuevo", new BarrioModel());
	     return "formularios/barrios";
	 }
	 @GetMapping("/barrioTable")
	 public String accesoTabla(Model model) {
	     System.out.println("Accediendo a la tabla");
	     List<BarrioModel>resultado=barrioSer.listar();
	     model.addAttribute("listaBarrios", resultado);
	     return "tablas/barrioTable";
	 }
	//guardar
		 @PostMapping("/guardarBarrio")
		 public String guardarNuevoUsuario(@ModelAttribute("nuevo")BarrioModel nuevoBarrio) 
		 {
			 barrioSer.insertar(nuevoBarrio);
			 return "redirect:barrioTable";
		 }
		 //editar
		 @GetMapping("/editarBarrio/{id}")
		 public String editarUsuario(Model model, @PathVariable("id") Long id) {
		     // Cargar el usuario desde la base de datos y pasarlo al modelo
		     model.addAttribute("usuario", barrioSer.buscarPorId(id));
		     
		     return "editarForm/editarBarrio"; // Carga la plantilla de edición
		 }
		 //eliminar
		 @GetMapping("/eliminarBarrio/{id}")
		 public String eliminarBarrio(@PathVariable("id") Long id) {
		     // Llamar al servicio para eliminar el usuario
		     barrioSer.eliminar(id);
		     return "redirect:/alerta/barrioTable";  // Redirigir a la tabla después de la eliminación
		 }	 
}
