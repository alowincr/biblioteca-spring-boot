package com.aluracursos.biblioteca;

import com.aluracursos.biblioteca.principal.Principal; // <-- tu nueva clase Principal
import com.aluracursos.biblioteca.repository.LibroRepository;
import com.aluracursos.biblioteca.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplicationConsola implements CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplicationConsola.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Le pasamos ambos repositorios si tu Principal los necesita
        Principal principal = new Principal(libroRepository, autorRepository);
        principal.muestraElMenu();
    }
}
