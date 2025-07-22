package com.uisrael.alerta.service;

import java.util.List;
import java.util.Optional;

import com.uisrael.alerta.model.UsuarioModel;


public interface UsuarioService {
	public void insertar(UsuarioModel usuario);
	public List<UsuarioModel>listar();
	public UsuarioModel buscarPorId(Long id);
	public void eliminar(Long id);
	Optional<UsuarioModel> buscarPorCorreo(String correo); // <- nombre más coherente con el resto
	List<UsuarioModel> listarPorBarrio(Long barrioId);
	public boolean existePresidenteEnBarrio(Long barrioId);
	List<UsuarioModel>listarPoliciasNoValidados();
	List<UsuarioModel> listarCiudadanosPendientes(Long barrioId);
}
