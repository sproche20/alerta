package com.uisrael.alerta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uisrael.alerta.model.NotificacionModel;

@Repository
public interface NotificacionRepository extends JpaRepository<NotificacionModel, Long>{
	@Override
	@EntityGraph(attributePaths = {"incidenteModel"})
	List<NotificacionModel>findAll();
	@Query("""
		    SELECT n FROM NotificacionModel n 
		    WHERE n.barrioModel.id = :barrioId 
		       OR (n.barrioModel IS NULL AND n.incidenteModel IS NOT NULL)
		""")
		List<NotificacionModel> buscarGeneralesYPorBarrio(@Param("barrioId") Long barrioId);


}
