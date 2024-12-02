package com.fiap.tc.logistica.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public ObjectMapper customObjectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.dateFormat(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")); // Define o
        // formato de
        // data e hora

        return builder.build();
    }

}
