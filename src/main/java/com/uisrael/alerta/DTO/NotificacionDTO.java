package com.uisrael.alerta.DTO;

public class NotificacionDTO {
    private Long id;
    private String mensaje;

    public NotificacionDTO(Long id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }

    // Getters y setters
    public Long getId() { return id; }
    public String getMensaje() { return mensaje; }
    public void setId(Long id) { this.id = id; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
