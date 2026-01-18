package com.aluracursos.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(

        @JsonProperty("title")
        String titulo,

        @JsonProperty("authors")
        List<DatosAutor> autores,

        @JsonProperty("languages")
        List<String> idiomas,

        @JsonProperty("download_count")
        Integer numeroDeDescargas,

        @JsonProperty("formats")
        Map<String, String> formats

) {}
