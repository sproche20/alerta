package com.uisrael.alerta.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uisrael.alerta.model.BarrioModel;
@Repository
public interface BarrioRepository extends JpaRepository<BarrioModel, Long>{
    Optional<BarrioModel> findByNombre(String nombre);
    Optional<BarrioModel>findById(Long id);
}
