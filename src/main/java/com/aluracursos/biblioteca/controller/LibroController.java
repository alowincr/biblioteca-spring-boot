package com.aluracursos.biblioteca.controller;

import com.aluracursos.biblioteca.dto.AutorDTO;
import com.aluracursos.biblioteca.dto.LibroDTO;
import com.aluracursos.biblioteca.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService servicio;

    // 1️⃣ Obtener todos los libros
    @GetMapping()
    public List<LibroDTO> obtenerTodosLosLibros() {
        return servicio.obtenerTodosLosLibros();
    }

    // 2️⃣ Buscar libro por ID
    @GetMapping("/{id}")
    public LibroDTO obtenerPorId(@PathVariable Long id) {
        return servicio.obtenerPorId(id);
    }

    // 3️⃣ Buscar libro por título (usa la API Gutendex si no está registrado)
    @GetMapping("/buscar")
    public LibroDTO buscarPorTitulo(@RequestParam String titulo) {
        return servicio.buscarPorTitulo(titulo);
    }

    // 4️⃣ Listar todos los autores registrados
    @GetMapping("/autores")
    public List<AutorDTO> obtenerTodosLosAutores() {
        return servicio.obtenerTodosLosAutores();
    }

    // 5️⃣ Listar autores vivos en un año
    @GetMapping("/autores/vivos/{anio}")
    public List<AutorDTO> autoresVivosEnAno(@PathVariable int anio) {
        return servicio.autoresVivosEnAno(anio);
    }

    // 6️⃣ Listar libros por idioma
    @GetMapping("/idioma/{codigoIdioma}")
    public List<LibroDTO> obtenerLibrosPorIdioma(@PathVariable String codigoIdioma) {
        return servicio.obtenerLibrosPorIdioma(codigoIdioma);
    }
}
