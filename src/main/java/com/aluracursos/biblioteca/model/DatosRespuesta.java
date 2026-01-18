package com.aluracursos.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosRespuesta(

        @JsonProperty("results")
        List<DatosLibro> resultados

) {}
