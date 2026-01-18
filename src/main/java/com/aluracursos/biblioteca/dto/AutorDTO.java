package com.aluracursos.biblioteca.dto;

public record AutorDTO(
        Long id,
        String nombre,
        Integer anoNacimiento,
        Integer anoFallecimiento
) {
}
