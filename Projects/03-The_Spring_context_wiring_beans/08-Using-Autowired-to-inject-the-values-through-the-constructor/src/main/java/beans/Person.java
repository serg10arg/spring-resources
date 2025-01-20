package beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Instruye a Spring para crear y agregar un bean al contexto del tipo de esta clase: Person.
@Component
public class Person {
    private final String name = "Ella";
    private final Parrot parrot;

    /**
     * Cuando Spring crea el bean de tipo Person, llama al constructor anotado con @Autowired.
     * Spring proporciona un bean de tipo Parrot desde su contexto como valor del parámetro.
     */
    @Autowired
    public Person(Parrot parrot) { // Constructor con dependencia
        this.parrot = parrot;
    }

    public String getName() {
        return name;
    }

    public Parrot getParrot() {
        return parrot;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', parrot=" + parrot + '}';
    }
}
