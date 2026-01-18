package com.aluracursos.biblioteca.model;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer anoNacimiento;
    private Integer anoFallecimiento; // null si sigue vivo

    public Autor() {}  // Constructor vacío obligatorio para JPA

    // Constructor desde DatosAutor (Gutendex)
    public Autor(DatosAutor datos) {
        this.nombre = datos.nombre();
        this.anoNacimiento = datos.anoNacimiento();
        this.anoFallecimiento = datos.anoFallecimiento();
    }


    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getAnoNacimiento() { return anoNacimiento; }
    public void setAnoNacimiento(Integer anoNacimiento) { this.anoNacimiento = anoNacimiento; }

    public Integer getAnoFallecimiento() { return anoFallecimiento; }
    public void setAnoFallecimiento(Integer anoFallecimiento) { this.anoFallecimiento = anoFallecimiento; }

    @Override
    public String toString() {
        return nombre +
                " (Nacimiento: " + anoNacimiento +
                ", Fallecimiento: " + (anoFallecimiento != null ? anoFallecimiento : "Vivo") + ")";
    }


}
