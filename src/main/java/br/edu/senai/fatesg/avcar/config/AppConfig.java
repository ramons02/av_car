package br.edu.senai.fatesg.avcar.config;

import br.edu.senai.fatesg.avcar.core.patterns.DatabaseConnectionSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DatabaseConnectionSingleton databaseConnectionSingleton() {
        return DatabaseConnectionSingleton.getInstance();
    }
}
