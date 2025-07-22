package com.uisrael.alerta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uisrael.alerta.model.UsuarioModel;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long>{
	@Override
	@EntityGraph(attributePaths = {"barrioModel"})
	List<UsuarioModel>findAll();
	Optional<UsuarioModel> findByCorreo(String correo);
	List<UsuarioModel> findByBarrioModelId(Long barrioId);
	boolean existsByBarrioModelIdAndRol(Long barrioId,String rol);
	List<UsuarioModel>findByRolAndEnabledFalse(String rol);
	List<UsuarioModel> findByRolAndEnabledFalseAndBarrioModel_Id(String rol, Long barrioId);

}
