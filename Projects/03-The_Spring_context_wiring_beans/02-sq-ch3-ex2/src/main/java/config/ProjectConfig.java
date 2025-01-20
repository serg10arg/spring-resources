package config;

import beans.Parrot;
import beans.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public Parrot parrot() {
        Parrot parrot = new Parrot();
        parrot.setName("Koko");
        return parrot;
    }

    @Bean
    public Person person() {
        Person person = new Person();
        person.setName("Ella");
        person.setParrot(parrot()); //Llamada directa al metodo parrot()
        return person;
    }
}
