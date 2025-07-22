package com.uisrael.alerta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_notificacion")
public class NotificacionModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
    private LocalDateTime fechaEnvio;
    @ManyToOne
    @JoinColumn(name = "incidente_id")
    private IncidenteModel incidenteModel;
    @ManyToOne
    private BarrioModel barrioModel;

    private String medio;
}
