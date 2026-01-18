package com.aluracursos.biblioteca.service;

import com.aluracursos.biblioteca.dto.AutorDTO;
import com.aluracursos.biblioteca.dto.LibroDTO;
import com.aluracursos.biblioteca.model.Autor;
import com.aluracursos.biblioteca.model.DatosLibro;
import com.aluracursos.biblioteca.model.Libro;
import com.aluracursos.biblioteca.repository.AutorRepository;
import com.aluracursos.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private final ConvierteDatos conversor = new ConvierteDatos();
    private final RestTemplate restTemplate = new RestTemplate();
    private final String GUTENDEX_URL = "https://gutendex.com/books/?search=";

    // 1️⃣ Obtener todos los libros
    public List<LibroDTO> obtenerTodosLosLibros() {
        return convierteDatos(libroRepository.findAll());
    }

    // 2️⃣ Obtener libro por ID
    public LibroDTO obtenerPorId(Long id) {
        Optional<Libro> libro = libroRepository.findById(id);
        return libro.map(this::mapearADTO).orElse(null);
    }

    // 3️⃣ Buscar libro por título, usar API Gutendex si no está en la base
    public LibroDTO buscarPorTitulo(String titulo) {
        Optional<Libro> libroOpt = libroRepository.findByTituloContainsIgnoreCase(titulo);
        if (libroOpt.isPresent()) {
            return mapearADTO(libroOpt.get());
        } else {
            // Llamada a Gutendex
            String url = GUTENDEX_URL + titulo.replace(" ", "+");
            String respuesta = restTemplate.getForObject(url, String.class);
            // Convertir JSON a DatosLibro (usa tu ConvierteDatos o Jackson)
            DatosLibro datosLibro = conversor.obtenerDatos(respuesta, DatosLibro.class);
            if (datosLibro != null) {
                Libro libro = new Libro(datosLibro);
                libroRepository.save(libro);
                return mapearADTO(libro);
            }
        }
        return null;
    }

    // 4️⃣ Listar todos los autores registrados
    public List<AutorDTO> obtenerTodosLosAutores() {
        return autorRepository.findAll().stream()
                .map(a -> new AutorDTO(a.getId(), a.getNombre(), a.getAnoNacimiento(), a.getAnoFallecimiento()))
                .collect(Collectors.toList());
    }

    // 5️⃣ Listar autores vivos en un año
    public List<AutorDTO> autoresVivosEnAno(int anio) {
        return autorRepository.findAll().stream()
                .filter(a -> (a.getAnoFallecimiento() == null || a.getAnoFallecimiento() >= anio)
                        && (a.getAnoNacimiento() != null && a.getAnoNacimiento() <= anio))
                .map(a -> new AutorDTO(a.getId(), a.getNombre(), a.getAnoNacimiento(), a.getAnoFallecimiento()))
                .collect(Collectors.toList());
    }

    // 6️⃣ Listar libros por idioma
    public List<LibroDTO> obtenerLibrosPorIdioma(String idioma) {
        return libroRepository.findAll().stream()
                .filter(l -> l.getIdiomas().contains(idioma))
                .map(this::mapearADTO)
                .collect(Collectors.toList());
    }

    // --- Métodos privados ---
    private List<LibroDTO> convierteDatos(List<Libro> libros) {
        return libros.stream().map(this::mapearADTO).collect(Collectors.toList());
    }

    private LibroDTO mapearADTO(Libro libro) {
        return new LibroDTO(libro.getId(), libro.getTitulo(),
                libro.getAutores().stream()
                        .map(Autor::getNombre)
                        .collect(Collectors.toList()),
                libro.getIdiomas(),
                libro.getGutendexUrl());
    }
}
