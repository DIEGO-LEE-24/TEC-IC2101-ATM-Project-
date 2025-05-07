package modelo;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement
public class Cliente {
    private String nombreCompleto;
    private String identificacion;
    private String telefono;
    private String email;
    private String fechaRegistro; 

    public Cliente() {
        // JAXB necesita constructor sin argumentos
    }

    public Cliente(String nombreCompleto, String identificacion, String telefono, String email) {
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("El nombre completo no puede estar vacío");
        }
        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("La identificación no puede estar vacía");
        }
        if (!Validacion.validarTelefono(telefono)) {
            throw new IllegalArgumentException("Formato de teléfono inválido");
        }
        if (!Validacion.validarEmail(email)) {
            throw new IllegalArgumentException("Formato de correo electrónico inválido");
        }

        this.nombreCompleto = nombreCompleto.trim();
        this.identificacion = identificacion.trim();
        this.telefono       = telefono;
        this.email          = email;
        this.fechaRegistro  = LocalDateTime.now().toString(); 
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (!Validacion.validarTelefono(telefono)) {
            throw new IllegalArgumentException("Formato de teléfono inválido");
        }
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!Validacion.validarEmail(email)) {
            throw new IllegalArgumentException("Formato de correo electrónico inválido");
        }
        this.email = email;
    }

    public String getFechaRegistroRaw() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaRegistro() {
        return LocalDateTime.parse(fechaRegistro);
    }
    
    @Override
    public String toString() {
        return nombreCompleto + " (" + identificacion + ")";
    }
}
