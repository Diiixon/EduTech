package Cursos.Cursos.Controller;

import Cursos.Cursos.model.Nota;
import Cursos.Cursos.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notas")
public class NotasController {
    @Autowired
    private NotaService notaService;
    @GetMapping("/inscripcion/{idInscripcion}")
    public ResponseEntity<List<Nota>> obtenerNotasPorInscripcion(@PathVariable Long idInscripcion){
        List<Nota> notas = notaService.obtenerNotasPorInscripcion(idInscripcion);
        return ResponseEntity.ok(notas);
    }
}
