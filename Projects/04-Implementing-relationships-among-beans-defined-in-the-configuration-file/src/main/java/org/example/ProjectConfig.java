package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {
    @Bean
    public Parrot parrot() {
        Parrot parrot = new Parrot();
        parrot.setName("Koko");
        return  parrot;
    }

    @Bean
    public Person person() {
        Person person = new Person();
        person.setName("Ella");
        return person;
    }
}