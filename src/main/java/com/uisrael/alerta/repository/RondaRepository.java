package com.uisrael.alerta.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uisrael.alerta.model.RondaModel;

@Repository
public interface RondaRepository extends JpaRepository<RondaModel, Long>{
	@Override
	@EntityGraph(attributePaths = {"barrioModel","policia"})
	List<RondaModel>findAll();
	boolean existsByFechaHoraAndBarrioModelId(LocalDateTime fechaHora, Long barrioModel_id);
}
