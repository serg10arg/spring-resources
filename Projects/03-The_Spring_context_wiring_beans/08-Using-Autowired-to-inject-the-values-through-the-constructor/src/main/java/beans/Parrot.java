package beans;

import org.springframework.stereotype.Component;

/**
 * La anotación de estereotipo @Component instruye a Spring para crear y agregar un bean al contexto del tipo de esta clase: Person
 */
@Component
public class Parrot {
    private String name = "Koko";

    public Parrot() {

    }

    public Parrot(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Parrot " + name;
    }
}
