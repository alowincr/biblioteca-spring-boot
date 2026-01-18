package com.aluracursos.biblioteca.dto;

import java.util.List;

public record LibroDTO(
        Long id,
        String titulo,
        List<String> autores,
        List<String> idiomas,
        String gutendexUrl
) {
}
