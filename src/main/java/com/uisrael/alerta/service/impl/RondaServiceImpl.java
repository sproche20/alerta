package com.uisrael.alerta.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uisrael.alerta.model.RondaModel;
import com.uisrael.alerta.repository.RondaRepository;
import com.uisrael.alerta.service.RondaService;

@Service
public class RondaServiceImpl implements RondaService{
	@Autowired
	private RondaRepository rondaRep;
	@Override
	public void insertar(RondaModel ronda) {
		try {
			if (ronda.getPolicia()==null) {
	            throw new IllegalArgumentException("Debe asignar un policía a la ronda");
			}
			if (!"POLICIA".equalsIgnoreCase(ronda.getPolicia().getRol())) {
	            throw new IllegalArgumentException("El usuario asignado no tiene rol de POLICIA");
			}
			if (rondaRep.existsByFechaHoraAndBarrioModelId(ronda.getFechaHora(), ronda.getBarrioModel().getId())) {
			    throw new IllegalArgumentException("Ya existe una ronda registrada para esa fecha y barrio.");
			}
			rondaRep.save(ronda);
		} catch (Exception e) {
			// TODO: handle exception
			 e.printStackTrace();
		}
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<RondaModel> listar() {
		// TODO Auto-generated method stub
		return rondaRep.findAll();
	}

	@Override
	public RondaModel buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return rondaRep.findById(id).orElse(null);
	}

	@Override
	public void eliminar(Long id) {
		// TODO Auto-generated method stub
		try {
			rondaRep.deleteById(id);
		} catch (Exception e) {
			// TODO: handle exception
	        e.printStackTrace();   
		}
		
	}

	@Override
	public boolean existeRondaEnFechaYBarrio(LocalDateTime fechaHora, Long barrioId) {
		// TODO Auto-generated method stub
		return rondaRep.existsByFechaHoraAndBarrioModelId(fechaHora, barrioId);
	}
}
