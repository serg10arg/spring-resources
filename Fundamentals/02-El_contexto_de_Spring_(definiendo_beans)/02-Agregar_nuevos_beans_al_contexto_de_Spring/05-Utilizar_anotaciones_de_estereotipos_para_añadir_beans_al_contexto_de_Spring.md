### **1. Introducción**
Spring Framework permite gestionar los objetos (beans) de una aplicación mediante su contexto. Existen varias formas de añadir beans al contexto de Spring, siendo dos de las más importantes: **anotaciones estereotípicas** (como `@Component`) y el método `@Bean`. Este resumen explora el uso práctico de las anotaciones estereotípicas, destacando cómo simplifican el proceso al reducir el código necesario.

---

### **2. Fundamentos de las Anotaciones Estereotípicas**
Las anotaciones estereotípicas, como `@Component`, indican a Spring que debe crear y gestionar una instancia de una clase específica dentro de su contexto.

#### **Pasos básicos para usar `@Component`**
1. **Anotar la clase**: Agrega `@Component` encima de la clase que deseas que Spring gestione como bean.
2. **Configurar la búsqueda**: Usa `@ComponentScan` en la clase de configuración para indicar a Spring dónde buscar clases anotadas.

#### **Ejemplo Práctico**
Definamos una clase `Parrot` que será gestionada por Spring:

```java
@Component // Marca esta clase como un componente gestionado por Spring
public class Parrot {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

Para que Spring encuentre esta clase, se configura lo siguiente:

```java
@Configuration // Define la clase como configuración para Spring
@ComponentScan(basePackages = "main") // Indica dónde buscar clases anotadas
public class ProjectConfig {
}
```

**Código de prueba en el método principal**:

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
        Parrot p = context.getBean(Parrot.class);
        System.out.println(p); // Representación del objeto Parrot
        System.out.println(p.getName()); // Imprimirá null porque aún no se asignó un nombre
    }
}
```

---

### **3. Comparación entre `@Bean` y Anotaciones Estereotípicas**
| **Característica**                     | **@Bean**                                                                                  | **Anotaciones Estereotípicas**                                        |
|----------------------------------------|-------------------------------------------------------------------------------------------|------------------------------------------------------------------------|
| **Control de creación**                | Total control sobre la instancia creada.                                                  | Control limitado; Spring crea la instancia automáticamente.           |
| **Instancias múltiples**               | Permite añadir múltiples instancias del mismo tipo.                                       | Solo una instancia por clase.                                         |
| **Compatibilidad con clases externas** | Se puede usar con clases externas (por ejemplo, clases de librerías).                    | Solo aplicable a clases de la aplicación.                             |
| **Boilerplate**                        | Requiere más código (métodos específicos para cada bean).                                 | Reduce el código necesario.                                           |

#### **Cuándo usar cada enfoque**
- Usa **`@Component`** para las clases propias de tu aplicación.
- Usa **`@Bean`** para añadir instancias de clases externas o cuando necesites personalización avanzada en su creación.

---

### **4. Personalización Posterior a la Creación: Uso de `@PostConstruct`**
Aunque `@Component` simplifica la creación de beans, a veces es necesario ejecutar lógica adicional justo después de que Spring cree la instancia. Esto se logra con `@PostConstruct`.

#### **Ejemplo Práctico**
```java
@Component
public class Parrot {
    private String name;

    @PostConstruct
    public void init() {
        this.name = "Kiki"; // Asigna un valor después de la creación
    }

    public String getName() {
        return name;
    }
}
```

Ahora, al ejecutar el programa, el método `init()` se invoca automáticamente después de que Spring crea el bean, asignando el nombre "Kiki".

---

### **5. Limpieza antes de la Destrucción: `@PreDestroy`**
Aunque menos usado en aplicaciones reales, `@PreDestroy` define una lógica que se ejecuta antes de que Spring cierre el contexto. Sin embargo, no es recomendado para operaciones críticas (como cerrar conexiones de base de datos) debido a posibles fallos en su ejecución.

#### **Ejemplo de uso de `@PreDestroy`**
```java
@Component
public class Parrot {
    @PreDestroy
    public void cleanup() {
        System.out.println("Limpiando recursos...");
    }
}
```

---

### **6. Consideraciones Prácticas**
1. **Reducción de Código**: Las anotaciones estereotípicas son ideales para aplicaciones donde la simplicidad y la rapidez en la configuración son prioridades.
2. **Flexibilidad**: `@Bean` sigue siendo esencial para casos donde se necesita un control completo o integración con clases externas.
3. **Configuración**: Asegúrate de usar `@ComponentScan` correctamente, indicando todos los paquetes relevantes.

---

### **7. Conclusión**
El uso de `@Component` y otras anotaciones estereotípicas ofrece una forma elegante y eficiente de gestionar beans en Spring. Este enfoque reduce el esfuerzo de codificación, al tiempo que mantiene la flexibilidad necesaria para aplicaciones complejas. Sin embargo, para escenarios que requieran personalización avanzada, el método `@Bean` sigue siendo una herramienta valiosa.