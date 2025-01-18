### **1. Introducción al Contexto de Spring**
El contexto de Spring es un contenedor que almacena y gestiona las instancias de objetos que necesita una aplicación. Es como un "almacén central" donde Spring mantiene los objetos y sus relaciones. Al agregar un objeto al contexto, Spring puede gestionarlo y aplicarle características del framework.

---

### **2. Prerrequisitos: Crear un Proyecto con Spring**
#### **2.1 Crear un Proyecto Maven**
Comenzamos creando un proyecto Maven básico sin dependencias de frameworks. Luego, agregamos las necesarias para trabajar con el contexto de Spring.

##### **Clase Base: `Parrot`**
Definimos una clase `Parrot` con un atributo `name` como ejemplo:

```java
public class Parrot {
    private String name;

    // Getters y setters omitidos para simplicidad
}
```

##### **Clase `Main`: Creación del Objeto**
Creamos una instancia de `Parrot` en una clase con el método `main`:

```java
public class Main {
    public static void main(String[] args) {
        Parrot p = new Parrot(); // Crear instancia de Parrot
    }
}
```

---

#### **2.2 Agregar Dependencias de Spring**
Añadimos la dependencia para el contexto de Spring en el archivo `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.2.6.RELEASE</version>
    </dependency>
</dependencies>
```

> **Nota**: Spring es modular, por lo que solo necesitamos incluir las partes específicas que utilizamos. En este caso, añadimos `spring-context` para manejar el contexto.

---

#### **2.3 Crear el Contexto de Spring**
Modificamos la clase `Main` para crear una instancia del contexto:

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(); // Crear el contexto de Spring
        Parrot p = new Parrot();
    }
}
```

> **Nota**: Usamos `AnnotationConfigApplicationContext` para trabajar con un enfoque basado en anotaciones.

---

### **3. Métodos para Agregar Beans al Contexto**
Spring ofrece tres formas principales para agregar beans al contexto, cada una con ventajas específicas:

#### **3.1 Usar la Anotación `@Bean`**
Este enfoque define un método en una clase de configuración que retorna una instancia del bean.

Ejemplo de una clase de configuración que agrega un `Parrot` al contexto:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Parrot parrot() {
        Parrot parrot = new Parrot();
        parrot.setName("Polly");
        return parrot;
    }
}
```

En este caso, Spring crea y gestiona el objeto `Parrot` retornado por el método `parrot()`.

---

#### **3.2 Usar Anotaciones de Estereotipo**
Las anotaciones como `@Component`, `@Service` y `@Repository` permiten marcar clases directamente como beans. Estas se detectan automáticamente mediante el escaneo de componentes.

Ejemplo:

```java
import org.springframework.stereotype.Component;

@Component
public class Parrot {
    private String name = "Polly";

    public String getName() {
        return name;
    }
}
```

Para habilitar el escaneo de componentes, configuramos el paquete base:

```java
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {
}
```

---

#### **3.3 Agregar Beans Programáticamente**
Podemos agregar instancias directamente al contexto mediante programación.

Ejemplo:

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext();
        context.registerBean("parrot", Parrot.class, () -> {
            Parrot parrot = new Parrot();
            parrot.setName("Polly");
            return parrot;
        });
    }
}
```

---

### **4. Comparación de Métodos**
| Método                  | Uso Recomendado                                   |
|-------------------------|---------------------------------------------------|
| `@Bean`                 | Configuración explícita de beans.                 |
| Anotaciones de estereotipo | Clases autodetectadas, ideal para estructuras modulares. |
| Programático            | Casos dinámicos o configuraciones en tiempo de ejecución. |

---

### **5. Ejemplo Completo**
Aquí se muestra un ejemplo combinado que utiliza `@Bean` y anotaciones de estereotipo.

##### **Clase `Parrot`:**
```java
public class Parrot {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

##### **Clase de Configuración:**
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public Parrot parrot() {
        Parrot parrot = new Parrot();
        parrot.setName("Polly");
        return parrot;
    }
}
```

##### **Clase `Main`:**
```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        Parrot parrot = context.getBean(Parrot.class);
        System.out.println("Parrot name: " + parrot.getName());
    }
}
```

---

### **6. Conclusión**
Agregar beans al contexto de Spring es un paso esencial para aprovechar las funcionalidades del framework. Los métodos presentados permiten flexibilidad para manejar diversos casos prácticos, desde configuraciones simples hasta escenarios dinámicos.