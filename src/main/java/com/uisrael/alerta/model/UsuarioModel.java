package com.uisrael.alerta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe ser válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;
    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 10, message = "La contraseña debe tener 10 caracteres")
    private String cedula;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "^(CIUDADANO|PRESIDENTE|POLICIA|ADMIN)$", message = "Rol no válido")
    private String rol;

    @NotNull(message = "Debe seleccionar un barrio")
    @ManyToOne
    @JoinColumn(name = "barrio_id")
    private BarrioModel barrioModel;

    private boolean enabled = true;
}

