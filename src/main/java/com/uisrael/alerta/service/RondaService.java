package com.uisrael.alerta.service;

import java.time.LocalDateTime;
import java.util.List;

import com.uisrael.alerta.model.RondaModel;

public interface RondaService {
	public void insertar(RondaModel ronda);
	public List<RondaModel>listar();
	public RondaModel buscarPorId(Long id);
	public void eliminar(Long id);
	public boolean existeRondaEnFechaYBarrio(LocalDateTime fechaHora, Long barrioId);
}
