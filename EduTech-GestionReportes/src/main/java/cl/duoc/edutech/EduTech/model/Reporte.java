package cl.duoc.edutech.EduTech.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reporte {

    private static long contador = 1;

    @JsonProperty("ID del Reporte")
    private Long id;
    @JsonProperty("Título")
    private String nombre;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("Fecha de Generación")
    private LocalDateTime fecha;
    @JsonProperty("Resumen")
    private String descripcion;

    @JsonProperty("Detalle")
    private List<Map<String, Object>> detalles;

    @JsonProperty("DetalleUnico")
    private Map<String, Object> detalleUnico; // Para reportes como sistema


    public Reporte(String nombre, String descripcion, List<Map<String, Object>> detalles) {
        this.id = contador++;
        this.nombre = nombre;
        this.fecha = LocalDateTime.now();
        this.descripcion = descripcion;
        this.detalles = detalles;
    }

    public Reporte(String nombre, String descripcion, Map<String, Object> detalleUnico) {
        this.id = contador++;
        this.nombre = nombre;
        this.fecha = LocalDateTime.now();
        this.descripcion = descripcion;
        this.detalleUnico = detalleUnico;
    }

}