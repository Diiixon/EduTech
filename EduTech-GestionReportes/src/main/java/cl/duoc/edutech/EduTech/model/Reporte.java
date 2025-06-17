package cl.duoc.edutech.EduTech.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reporte {

    private static long contador = 1;

    @Schema(description = "Identificador único del reporte", example = "1")
    @JsonProperty("ID del Reporte")
    private Long id;

    @Schema(description = "Título del reporte generado", example = "Reporte Total Estudiantes")
    @JsonProperty("Título")
    private String nombre;

    @Schema(description = "Fecha y hora en que se genera el reporte", example = "15-06-2025 14:30:00")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("Fecha de Generación")
    private LocalDateTime fecha;

    @Schema(description = "Resumen o descripción del contenido del reporte", example = "Total de estudiantes: 50")
    @JsonProperty("Resumen")
    private String descripcion;

    @Schema(description = "Lista con los datos detallados del reporte")
    @JsonProperty("Detalle")
    private List<Map<String, Object>> detalles;

    @Schema(description = "Detalle único (por ejemplo, uso del sistema: CPU, RAM, Disco)", nullable = true,
            example = "{\"CPU\": \"25%\", \"RAM\": \"8 GB de 16 GB\", \"Disco\": \"120 GB de 500 GB\"}")
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