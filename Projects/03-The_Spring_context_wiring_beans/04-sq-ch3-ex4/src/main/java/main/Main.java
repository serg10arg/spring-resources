package main;

import beans.Person;
import config.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // Crea el contexto de Spring usando la configuración especificada en ProjectConfig
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        // Obtener el bean de un tipo Person
        Person person = context.getBean(Person.class);

        // Imprimir información de la persona y su loro
        System.out.println("Person's name: " + person.getName());
        System.out.println("Person's parrot: " + person.getParrot());

    }
}
