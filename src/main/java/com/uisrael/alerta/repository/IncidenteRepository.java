package com.uisrael.alerta.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uisrael.alerta.model.BarrioModel;
import com.uisrael.alerta.model.IncidenteModel;
@Repository
public interface IncidenteRepository extends JpaRepository<IncidenteModel, Long>{
	@Override
	@EntityGraph(attributePaths = {"usuarioModel","barrioModel"})
	List<IncidenteModel>findAll();
	List<IncidenteModel> findByBarrioModelId(Long barrioId);
	List<IncidenteModel> findByFechaHoraAfter(LocalDateTime fecha);

}
