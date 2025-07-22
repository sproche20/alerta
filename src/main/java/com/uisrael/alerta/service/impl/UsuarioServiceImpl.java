package com.uisrael.alerta.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uisrael.alerta.model.UsuarioModel;
import com.uisrael.alerta.repository.UsuarioRepository;
import com.uisrael.alerta.service.UsuarioService;
@Service
public class UsuarioServiceImpl implements UsuarioService{
	@Autowired
	private UsuarioRepository userRep;

	@Override
	public void insertar(UsuarioModel usuario) {
		// TODO Auto-generated method stub
		try {
			userRep.save(usuario);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<UsuarioModel> listar() {
		// TODO Auto-generated method stub
		return userRep.findAll();
	}

	@Override
	public UsuarioModel buscarPorId(Long id) {
	    return userRep.findById(id)
	                  .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
	}


	@Override
	public void eliminar(Long id) {
		// TODO Auto-generated method stub
				try {
					userRep.deleteById(id);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
	}

	@Override
	public Optional<UsuarioModel> buscarPorCorreo(String correo) {
		// TODO Auto-generated method stub
		return userRep.findByCorreo(correo);
	}

	@Override
	public List<UsuarioModel> listarPorBarrio(Long barrioId) {
		// TODO Auto-generated method stub
		return userRep.findByBarrioModelId(barrioId);
	}

	@Override
	public boolean existePresidenteEnBarrio(Long barrioId) {
		// TODO Auto-generated method stub
		return userRep.existsByBarrioModelIdAndRol(barrioId, "PRESIDENTE");
	}

	@Override
	public List<UsuarioModel> listarPoliciasNoValidados() {
		// TODO Auto-generated method stub
		return userRep.findByRolAndEnabledFalse("POLICIA");
	}

	@Override
	public List<UsuarioModel> listarCiudadanosPendientes(Long barrioId) {
		// TODO Auto-generated method stub
	  return userRep.findByRolAndEnabledFalseAndBarrioModel_Id("CIUDADANO", barrioId);
	}

	
}
