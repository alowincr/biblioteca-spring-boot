package com.aluracursos.biblioteca.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoAPI {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // Método genérico para obtener JSON de cualquier URL
    public String obtenerDatos(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al consumir API: " + e.getMessage(), e);
        }
    }

    // Método genérico para convertir JSON a objeto Java (DTO)
    public <T> T obtenerDatos(String url, Class<T> claseDTO) {
        String json = obtenerDatos(url);
        try {
            return mapper.readValue(json, claseDTO);
        } catch (IOException e) {
            throw new RuntimeException("Error al convertir JSON: " + e.getMessage(), e);
        }
    }
}
