package com.uisrael.alerta.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uisrael.alerta.model.NotificacionModel;
import com.uisrael.alerta.repository.NotificacionRepository;
import com.uisrael.alerta.service.NotificacionService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificacionesServiceImpl implements NotificacionService{
	@Autowired
	private NotificacionRepository notificacionRep;
	@Override
	public void insertar(NotificacionModel notificacion) {
		// TODO Auto-generated method stub
		try {
			notificacionRep.save(notificacion);
		} catch (Exception e) {
			 e.printStackTrace();
			// TODO: handle exception
		}
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<NotificacionModel> listar() {
		// TODO Auto-generated method stub
		return notificacionRep.findAll();
	}

	@Override
	public NotificacionModel buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return notificacionRep.findById(id).get();
	}

	@Override
	public void eliminar(Long id) {
		// TODO Auto-generated method stub
		try {
			notificacionRep.deleteById(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	@Override
	public List<NotificacionModel> buscarGeneralesYPorBarrio(Long barrioId) {
	    return notificacionRep.buscarGeneralesYPorBarrio(barrioId);
	}
}
