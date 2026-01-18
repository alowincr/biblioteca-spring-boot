package com.aluracursos.biblioteca.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private Integer numeroDeDescargas;

    @Column(name = "gutendex_url", length = 1000)
    private String gutendexUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "libro_idiomas",
            joinColumns = @JoinColumn(name = "libro_id")
    )
    @Column(name = "idioma")
    private List<String> idiomas = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();

    // ================= CONSTRUCTORES =================

    public Libro() {
    }

    public Libro(DatosLibro datos) {
        this.titulo = datos.titulo();
        this.numeroDeDescargas = datos.numeroDeDescargas();
        this.idiomas = datos.idiomas();

        if (datos.formats() != null) {
            this.gutendexUrl = datos.formats().get("text/html");
        }
    }


    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public String getGutendexUrl() {
        return gutendexUrl;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    // ================= toString =================

    @Override
    public String toString() {
        return """
                -----------------------------
                Título: %s
                Autor(es): %s
                Idiomas: %s
                Descargas: %s
                -----------------------------
                """.formatted(
                titulo != null ? titulo : "N/A",
                autores.isEmpty()
                        ? "Desconocido"
                        : autores.stream().map(Autor::getNombre).toList(),
                idiomas.isEmpty() ? "N/A" : idiomas,
                numeroDeDescargas != null ? numeroDeDescargas : "N/A"
        );
    }
}
