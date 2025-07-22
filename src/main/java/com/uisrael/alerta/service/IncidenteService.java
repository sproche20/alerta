package com.uisrael.alerta.service;

import java.time.LocalDateTime;
import java.util.List;

import com.uisrael.alerta.model.IncidenteModel;
import com.uisrael.alerta.model.UsuarioModel;

public interface IncidenteService {
	public void insertar(IncidenteModel incidente);
	public List<IncidenteModel>listar();
	public IncidenteModel buscarPorId(Long id);
	public void eliminar(Long id);
	public List<IncidenteModel>listarPorBarrio(Long barrioId);
	List<IncidenteModel> buscarDesde(LocalDateTime fechaDesde);

}
