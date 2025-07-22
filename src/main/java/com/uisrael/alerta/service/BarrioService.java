package com.uisrael.alerta.service;

import java.util.List;

import com.uisrael.alerta.model.BarrioModel;

public interface BarrioService {
	public void insertar(BarrioModel barrio);
	public List<BarrioModel>listar();
	public BarrioModel buscarPorId(Long id);
	public void eliminar(Long id);
}
