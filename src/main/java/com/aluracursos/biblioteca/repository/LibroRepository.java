package com.aluracursos.biblioteca.repository;

import com.aluracursos.biblioteca.model.Autor;
import com.aluracursos.biblioteca.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Buscar libro por título (ignore mayúsculas/minúsculas)
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    // Buscar libros por idioma
    List<Libro> findByIdiomasContaining(String idioma);

    // Listar todos los libros de un autor (por nombre)
    @Query("SELECT l FROM Libro l JOIN l.autores a WHERE a.nombre ILIKE %:nombreAutor%")
    List<Libro> findByAutorNombre(String nombreAutor);

    // Listar los top 5 libros por título más recientes en Gutendex (puede ser por ID o fecha si la guardas)
    @Query("SELECT l FROM Libro l ORDER BY l.id DESC") // si quieres lo más reciente, usa ID descendente
    List<Libro> top5LibrosRecientes();

    // Buscar autores vivos en un año
    @Query("SELECT a FROM Autor a WHERE (a.anoFallecimiento IS NULL OR a.anoFallecimiento >= :anio) AND (a.anoNacimiento <= :anio)")
    List<Autor> autoresVivosEnAno(int anio);

}
