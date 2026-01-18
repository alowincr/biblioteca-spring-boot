package com.aluracursos.biblioteca.principal;

import com.aluracursos.biblioteca.model.*;
import com.aluracursos.biblioteca.repository.AutorRepository;
import com.aluracursos.biblioteca.repository.LibroRepository;
import com.aluracursos.biblioteca.service.ConsumoAPI;
import com.aluracursos.biblioteca.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/?search=";

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    private List<Libro> libros;

    // ✅ Constructor correcto (inyección manual para consola)
    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosEnAno();
                case 5 -> listarLibrosPorIdioma();
                case 0 -> System.out.println("Cerrando la aplicación...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    // =============================
    // BUSCAR Y GUARDAR LIBRO
    // =============================
    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el título del libro que deseas buscar: ");
        String titulo = teclado.nextLine();

        String json = consumoApi.obtenerDatos(URL_BASE + titulo.replace(" ", "+"));
        DatosRespuesta respuesta = conversor.obtenerDatos(json, DatosRespuesta.class);

        if (respuesta.resultados() == null || respuesta.resultados().isEmpty()) {
            System.out.println("No se encontró el libro.");
            return;
        }

        DatosLibro datos = respuesta.resultados().get(0);

        // Crear libro SIN autores
        Libro libro = new Libro(datos);

        // Manejo correcto de autores (sin duplicados)
        List<Autor> autores = datos.autores().stream()
                .map(datoAutor ->
                        autorRepository
                                .findByNombreIgnoreCase(datoAutor.nombre())
                                .orElseGet(() -> autorRepository.save(new Autor(datoAutor)))
                )
                .toList();

        libro.setAutores(autores);
        libroRepository.save(libro);


        System.out.println("""
                -----------------------------
                Libro registrado desde Gutendex:
                %s
                -----------------------------
                """.formatted(libro));
    }

    // =============================
    // LISTAR LIBROS
    // =============================
    private void listarLibrosRegistrados() {
        libros = libroRepository.findAll();
        System.out.println("=== Libros registrados ===");
        libros.forEach(System.out::println);
    }

    // =============================
    // LISTAR AUTORES
    // =============================
    private void listarAutoresRegistrados() {
        libros = libroRepository.findAll();

        Set<Autor> autores = libros.stream()
                .flatMap(libro -> libro.getAutores().stream())
                .collect(Collectors.toSet());

        System.out.println("=== Autores registrados ===");
        autores.forEach(System.out::println);
    }

    // =============================
    // AUTORES VIVOS EN AÑO
    // =============================
    private void listarAutoresVivosEnAno() {
        System.out.println("Ingrese el año: ");
        int ano = teclado.nextInt();
        teclado.nextLine();

        libros = libroRepository.findAll();

        Set<Autor> autoresVivos = libros.stream()
                .flatMap(libro -> libro.getAutores().stream())
                .filter(a ->
                        a.getAnoNacimiento() != null &&
                                a.getAnoNacimiento() <= ano &&
                                (a.getAnoFallecimiento() == null || a.getAnoFallecimiento() >= ano)
                )
                .collect(Collectors.toSet());

        if (autoresVivos.isEmpty()) {
            System.out.println("No hay autores registrados vivos en el año " + ano);
        } else {
            System.out.println("=== Autores vivos en " + ano + " ===");
            autoresVivos.forEach(System.out::println);
        }
    }


    // =============================
    // LIBROS POR IDIOMA
    // =============================
    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma del libro (ej: en, es, fr): ");
        String idioma = teclado.nextLine().trim().toLowerCase();

        libros = libroRepository.findAll();

        List<Libro> librosPorIdioma = libros.stream()
                .filter(l -> l.getIdiomas().stream()
                        .anyMatch(i -> i.equalsIgnoreCase(idioma)))
                .collect(Collectors.toList());

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No hay libros registrados en el idioma: " + idioma);
        } else {
            System.out.println("=== Libros en idioma " + idioma + " ===");
            librosPorIdioma.forEach(System.out::println);
        }
    }
}
