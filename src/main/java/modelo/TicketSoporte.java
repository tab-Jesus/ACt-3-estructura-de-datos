package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Entidad principal del sistema de Mesa de Ayuda TI.
 * Representa un ticket de soporte técnico registrado en el sistema.
 */
public class TicketSoporte {

    private String codigoTicket;
    private String nombreUsuario;
    private String descripcionProblema;
    private String categoria;
    private String prioridad;
    private String estado;
    private LocalDateTime fechaRegistro;
    private String tecnicoAsignado;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TicketSoporte(String codigoTicket, String nombreUsuario,
                         String descripcionProblema, String categoria,
                         String prioridad, String tecnicoAsignado) {
        this.codigoTicket        = codigoTicket;
        this.nombreUsuario       = nombreUsuario;
        this.descripcionProblema = descripcionProblema;
        this.categoria           = categoria.toUpperCase();
        this.prioridad           = prioridad.toUpperCase();
        this.estado              = "PENDIENTE";
        this.fechaRegistro       = LocalDateTime.now();
        this.tecnicoAsignado     = tecnicoAsignado;
    }

    public String getCodigoTicket()        { return codigoTicket; }
    public String getNombreUsuario()       { return nombreUsuario; }
    public String getDescripcionProblema() { return descripcionProblema; }
    public String getCategoria()           { return categoria; }
    public String getPrioridad()           { return prioridad; }
    public String getEstado()              { return estado; }
    public LocalDateTime getFechaRegistro(){ return fechaRegistro; }
    public String getTecnicoAsignado()     { return tecnicoAsignado; }

    public void setCodigoTicket(String codigoTicket)             { this.codigoTicket = codigoTicket; }
    public void setNombreUsuario(String nombreUsuario)           { this.nombreUsuario = nombreUsuario; }
    public void setDescripcionProblema(String descripcionProblema){ this.descripcionProblema = descripcionProblema; }
    public void setCategoria(String categoria)                   { this.categoria = categoria.toUpperCase(); }
    public void setPrioridad(String prioridad)                   { this.prioridad = prioridad.toUpperCase(); }
    public void setEstado(String estado)                         { this.estado = estado.toUpperCase(); }
    public void setFechaRegistro(LocalDateTime fechaRegistro)    { this.fechaRegistro = fechaRegistro; }
    public void setTecnicoAsignado(String tecnicoAsignado)       { this.tecnicoAsignado = tecnicoAsignado; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketSoporte)) return false;
        TicketSoporte that = (TicketSoporte) o;
        return Objects.equals(codigoTicket, that.codigoTicket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoTicket);
    }

    @Override
    public String toString() {
        return String.format(
            "╔══════════════════════════════════════════════════════╗%n" +
            "  Ticket : %-10s  Usuario: %-20s%n" +
            "  Categ. : %-12s Prioridad: %-6s Estado: %s%n" +
            "  Técnico: %-20s Fecha: %s%n" +
            "  Problema: %s%n" +
            "╚══════════════════════════════════════════════════════╝",
            codigoTicket, nombreUsuario,
            categoria, prioridad, estado,
            tecnicoAsignado, fechaRegistro.format(FORMATTER),
            descripcionProblema
        );
    }

    /**
     * Versión resumida para mostrar en listas.
     */
    public String toStringResumen() {
        return String.format("[%s] %-20s | %-10s | %-6s | %-10s | Técnico: %s",
                codigoTicket, nombreUsuario, categoria, prioridad, estado, tecnicoAsignado);
    }
}
