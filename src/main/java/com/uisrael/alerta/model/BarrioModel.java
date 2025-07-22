package com.uisrael.alerta.model;

//Barrio.java

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_barrio")
public class BarrioModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del barrio es obligatorio")
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @OneToMany(mappedBy = "barrioModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioModel> usuarioModels = new ArrayList<>();

    @OneToMany(mappedBy = "barrioModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncidenteModel> incidenteModels = new ArrayList<>();
}
