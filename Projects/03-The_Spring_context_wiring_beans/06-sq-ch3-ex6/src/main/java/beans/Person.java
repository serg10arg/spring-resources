package beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Person {
    private final String name = "Ella";
    private Parrot parrot;

    public String getName() {
        return name;
    }

    public Parrot getParrot() {
        return parrot;
    }

    @Autowired
    public void setParrot(Parrot parrot) { // Inyección mediante setter
        this.parrot = parrot;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', parrot=" + parrot + '}';
    }
}
