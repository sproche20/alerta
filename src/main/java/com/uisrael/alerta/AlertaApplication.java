package com.uisrael.alerta;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.uisrael.alerta.model.BarrioModel;
import com.uisrael.alerta.repository.BarrioRepository;

@SpringBootApplication
public class AlertaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlertaApplication.class, args);
    }

    @Bean
    public CommandLineRunner cargarBarrios(BarrioRepository barrioRepo) {
        return args -> {
            if (barrioRepo.count() == 0) {
                barrioRepo.save(new BarrioModel(null, "San Carlos", "Quito", new ArrayList<>(), new ArrayList<>()));
                barrioRepo.save(new BarrioModel(null, "La Kennedy", "Guayaquil", new ArrayList<>(), new ArrayList<>()));
                barrioRepo.save(new BarrioModel(null, "El Ejido", "Cuenca", new ArrayList<>(), new ArrayList<>()));
            }
        };
    }
}

