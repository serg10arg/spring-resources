### **1. Fundamentos de la Anotación `@Bean`**

#### **¿Qué es un Bean en Spring?**
Un bean es un objeto gestionado por el contenedor de Spring. Solo los objetos que están en el contexto de Spring pueden ser administrados por el framework.

#### **¿Qué es la Anotación `@Bean`?**
La anotación `@Bean` indica a Spring que el método anotado producirá un bean que debe ser agregado al contexto de la aplicación.

#### **Pasos para Usar `@Bean`**

1. **Crear una Clase de Configuración**
    - Se anota con `@Configuration` y se utiliza para definir configuraciones relacionadas con Spring.
2. **Definir un Método que Retorne la Instancia del Bean**
    - El método debe estar anotado con `@Bean` y devolver la instancia que se agregará al contexto.
3. **Inicializar el Contexto con la Clase de Configuración**
    - Se utiliza `AnnotationConfigApplicationContext` y se le pasa la clase de configuración.

---

### **2. Ejemplo Práctico: Añadir un Bean al Contexto**

#### **Definir la Clase de Configuración**

```java
@Configuration
public class ProjectConfig {
    @Bean
    Parrot parrot() {
        var p = new Parrot();
        p.setName("Koko");
        return p;
    }
}
```

- **`@Configuration`**: Indica que esta clase es una configuración para Spring.
- **Método `parrot`**: Devuelve un objeto de tipo `Parrot` con el nombre `Koko`. El nombre del método es el nombre del bean.

#### **Inicializar el Contexto y Usar el Bean**

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
        Parrot p = context.getBean(Parrot.class);
        System.out.println(p.getName()); // Output: Koko
    }
}
```

- **`AnnotationConfigApplicationContext`**: Inicializa el contexto usando la clase de configuración.
- **`getBean`**: Obtiene la instancia del bean por su tipo.

---

### **3. Añadir Múltiples Beans de Tipos Diferentes**

Puedes agregar múltiples beans de distintos tipos en la misma clase de configuración.

#### **Ejemplo**

```java
@Configuration
public class ProjectConfig {
    @Bean
    Parrot parrot() {
        var p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    String hello() {
        return "Hello";
    }

    @Bean
    Integer ten() {
        return 10;
    }
}
```

#### **Usar los Nuevos Beans**

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
        
        Parrot p = context.getBean(Parrot.class);
        System.out.println(p.getName()); // Output: Koko

        String s = context.getBean(String.class);
        System.out.println(s); // Output: Hello

        Integer n = context.getBean(Integer.class);
        System.out.println(n); // Output: 10
    }
}
```

- **Spring maneja diferentes tipos automáticamente**: No se requiere conversión explícita para obtener beans.

---

### **4. Añadir Múltiples Beans del Mismo Tipo**

Puedes agregar más de un bean del mismo tipo. Para diferenciarlos, cada método debe tener un nombre único.

#### **Ejemplo**

```java
@Configuration
public class ProjectConfig {
    @Bean
    Parrot parrot1() {
        var p = new Parrot();
        p.setName("Koko");
        return p;
    }

    @Bean
    Parrot parrot2() {
        var p = new Parrot();
        p.setName("Miki");
        return p;
    }
}
```

#### **Referirse a Beans por Nombre**

```java
public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        Parrot p1 = context.getBean("parrot1", Parrot.class);
        System.out.println(p1.getName()); // Output: Koko

        Parrot p2 = context.getBean("parrot2", Parrot.class);
        System.out.println(p2.getName()); // Output: Miki
    }
}
```

- **`getBean(String, Class)`**: Se usa para obtener un bean por su nombre y tipo.

---

### **5. Mejores Prácticas y Consideraciones**

- **Organización del Código**: Mantén las clases organizadas en paquetes como `config`, `main`, etc.
- **Uso del Contexto**: Solo agrega al contexto los objetos que Spring necesita gestionar.
- **Nombres de Métodos**: Usa sustantivos que representen los objetos retornados, ya que estos nombres se convierten en los nombres de los beans.
- **Pruebas**: Realiza pruebas unitarias e integrales para validar el comportamiento del contexto.

---

### **6. Aplicaciones del Mundo Real**

- **Gestión de Dependencias**: Define beans para servicios y repositorios, asegurando su inyección automática.
- **Configuración Centralizada**: Centraliza configuraciones, como fuentes de datos o propiedades.
- **Personalización**: Crea instancias personalizadas de clases externas, como configuraciones de bibliotecas.

---

### **Conclusión**

El uso de la anotación `@Bean` es crucial para gestionar beans en aplicaciones Spring. Comprender cómo definir, usar y organizar beans te permite construir aplicaciones escalables y mantenibles. Este conocimiento sirve como base para implementar patrones avanzados como inyección de dependencias, programación modular y servicios reutilizables.