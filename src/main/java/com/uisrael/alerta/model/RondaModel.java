package com.uisrael.alerta.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_ronda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RondaModel implements Serializable{
    private static final long serialVersionUID = 1L;
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 @NotNull
	 private LocalDateTime fechaHora;

	 @ManyToOne
	 @JoinColumn(name = "barrio_id")
	 private BarrioModel barrioModel;

	 @ManyToOne
	 @JoinColumn(name = "policia_id")
	 private UsuarioModel policia;

	 @Size(max = 255)
	 private String observaciones;
}
