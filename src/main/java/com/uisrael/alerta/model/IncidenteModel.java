package com.uisrael.alerta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_incidente")
public class IncidenteModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String descripcion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private LocalDateTime fechaHora;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuarioModel;

    @ManyToOne
    @JoinColumn(name = "barrio_id")
    private BarrioModel barrioModel;
    
    @OneToMany(mappedBy = "incidenteModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificacionModel> notificacionModels = new ArrayList<>();
    
    private Double latitud;
    private Double longitud;

}

