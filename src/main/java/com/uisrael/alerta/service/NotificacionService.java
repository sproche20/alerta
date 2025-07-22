package com.uisrael.alerta.service;

import java.util.List;

import com.uisrael.alerta.model.NotificacionModel;


public interface NotificacionService {
	public void insertar(NotificacionModel notificacion);
	public List<NotificacionModel>listar();
	public NotificacionModel buscarPorId(Long id);
	List<NotificacionModel> buscarGeneralesYPorBarrio(Long barrioId);
	public void eliminar(Long id);
}
