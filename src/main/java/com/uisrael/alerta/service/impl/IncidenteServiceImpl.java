package com.uisrael.alerta.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uisrael.alerta.model.IncidenteModel;
import com.uisrael.alerta.repository.IncidenteRepository;
import com.uisrael.alerta.service.IncidenteService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IncidenteServiceImpl implements IncidenteService{
	@Autowired
	private IncidenteRepository incidenteRep;

	@Override
	public void insertar(IncidenteModel incidente) {
		// TODO Auto-generated method stub
		try {
			incidenteRep.save(incidente);
		} catch (Exception e) {
			 e.printStackTrace();
			// TODO: handle exception
		}
	}
	@Override
	@Transactional(readOnly = true)
	public List<IncidenteModel> listar() {
		return incidenteRep.findAll();
	}

	@Override
	public IncidenteModel buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return incidenteRep.findById(id).orElse(null);
	}
	@Override
	public void eliminar(Long id) {
		try {
			incidenteRep.deleteById(id);  // Usar el método deleteById del repositorio para eliminar al usuario
	    } catch (Exception e) {
	        e.printStackTrace();   
	        }
		}
	@Override
	public List<IncidenteModel> listarPorBarrio(Long barrioId) {
		// TODO Auto-generated method stub
		return incidenteRep.findByBarrioModelId(barrioId);
	}
	@Override
	public List<IncidenteModel> buscarDesde(LocalDateTime fechaDesde) {
	    return incidenteRep.findByFechaHoraAfter(fechaDesde);
	}

}
