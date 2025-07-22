package com.uisrael.alerta.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uisrael.alerta.model.BarrioModel;
import com.uisrael.alerta.repository.BarrioRepository;
import com.uisrael.alerta.service.BarrioService;
@Service
public class BarrioServiceImpl implements BarrioService{
@Autowired
private BarrioRepository barrioRep;
	@Override
	public void insertar(BarrioModel barrio) {
		try {
			barrioRep.saveAndFlush(barrio);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}

	@Override
	public List<BarrioModel> listar() {
		// TODO Auto-generated method stub
		return barrioRep.findAll();
	}

	@Override
	public BarrioModel buscarPorId(Long id) {
		// TODO Auto-generated method stub
		return barrioRep.findById(id).orElse(null);
	}

	@Override
	public void eliminar(Long id) {
		// TODO Auto-generated method stub
				try {
					barrioRep.deleteById(id);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
		
	}

}
