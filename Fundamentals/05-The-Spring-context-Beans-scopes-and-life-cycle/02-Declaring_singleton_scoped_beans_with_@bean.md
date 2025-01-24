Para explicar el concepto de **declarar beans con alcance Singleton usando la anotación `@Bean` en Spring**, utilizaremos el enfoque de **primeros principios**, desglosando el tema en pasos claros y sencillos. Esta explicación está dirigida a personas con conocimientos básicos de programación pero que son nuevas en Spring.

---

### **1. ¿Qué es un 'bean' en Spring y por qué es importante?**

#### **Definición de bean**:
- En Spring, un **bean** es un objeto que es gestionado por el **contenedor de Spring**. Estos objetos son creados, configurados y gestionados por Spring, lo que facilita su uso en la aplicación.
- Los beans son los componentes básicos de una aplicación Spring. Pueden ser servicios, repositorios, configuraciones, o cualquier otro objeto que necesites en tu aplicación.

#### **Importancia de los beans**:
- **Inyección de dependencias**: Spring se encarga de crear y proporcionar los objetos que necesitas, lo que reduce el acoplamiento entre clases.
- **Gestión centralizada**: Spring gestiona el ciclo de vida de los beans, lo que facilita su configuración y reutilización.
- **Modularidad**: Los beans permiten dividir la aplicación en componentes independientes, lo que mejora la organización y mantenibilidad del código.

**Ejemplo práctico**: Piensa en un bean como una "pieza de Lego" que puedes usar para construir una aplicación. Spring es como el "manual de instrucciones" que te dice cómo y cuándo usar cada pieza.

---

### **2. ¿Qué significa el alcance 'singleton' y cómo se diferencia de otros alcances?**

#### **Definición de alcance Singleton**:
- En Spring, el alcance **Singleton** significa que solo existe **una instancia** de un bean en el contexto de Spring. Cada vez que solicitas ese bean, Spring te devuelve la misma instancia.
- Este es el alcance **predeterminado** en Spring, y es el más comúnmente utilizado.

#### **Diferencias con otros alcances**:
- **Prototype**: En este alcance, Spring crea una **nueva instancia** del bean cada vez que se solicita.
- **Request y Session**: Estos alcances están relacionados con aplicaciones web. Un bean con alcance **Request** se crea una vez por cada solicitud HTTP, mientras que un bean con alcance **Session** se crea una vez por cada sesión de usuario.

**Ejemplo práctico**: Imagina que tienes una impresora en una oficina. Si la impresora es un Singleton, todos los empleados usan la misma impresora. Si es un Prototype, cada empleado obtiene una impresora nueva cada vez que la necesita.

---

### **3. ¿Cómo funciona la anotación `@Bean` y cuál es su papel en la configuración de Spring?**

#### **Definición de `@Bean`**:
- La anotación `@Bean` se utiliza en métodos dentro de una clase de configuración (anotada con `@Configuration`) para definir un bean que Spring gestionará.
- Spring ejecuta el método anotado con `@Bean` y registra el objeto devuelto como un bean en su contexto.

#### **Papel de `@Bean`**:
- **Creación de beans**: Permite definir cómo se crea un bean y cómo se configura.
- **Personalización**: Puedes personalizar la creación del bean, por ejemplo, pasando dependencias o configurando propiedades.
- **Control**: Te da control sobre la instanciación de los beans, lo que es útil cuando no puedes o no quieres usar anotaciones como `@Component`.

**Ejemplo práctico**: Piensa en `@Bean` como una "receta" que le dices a Spring para crear un objeto. Spring sigue la receta y te devuelve el objeto listo para usar.

---

### **4. Ejemplo práctico de código**

#### **Declarar un bean Singleton con `@Bean`**:
Aquí tienes un ejemplo de cómo declarar un bean Singleton usando la anotación `@Bean`:

```java
@Configuration
public class AppConfig {

    @Bean
    public CommentService commentService() {
        return new CommentService();
    }
}
```

- **`@Configuration`**: Indica que esta clase es una clase de configuración de Spring.
- **`@Bean`**: Define un bean de tipo `CommentService`. Spring gestionará este bean como un Singleton.
- **`commentService()`**: Este método crea y devuelve una instancia de `CommentService`. Spring ejecuta este método y registra el objeto devuelto como un bean.

#### **Uso del bean**:
En la clase principal, puedes obtener el bean del contexto de Spring:

```java
public class Main {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Obtener el bean Singleton
        var commentService1 = context.getBean(CommentService.class);
        var commentService2 = context.getBean(CommentService.class);

        // Verificar si son la misma instancia
        System.out.println(commentService1 == commentService2); // true
    }
}
```

- **`AnnotationConfigApplicationContext`**: Crea el contexto de Spring usando la configuración definida en `AppConfig`.
- **`getBean(CommentService.class)`**: Obtiene el bean `CommentService` del contexto de Spring.
- **`commentService1 == commentService2`**: Devuelve `true` porque Spring está gestionando el bean como un Singleton.

---

### **5. Analogía para entender el concepto**

Imagina que Spring es un **restaurante** y los beans son los **platos** que se sirven.

- **`@Bean`**: Es como la receta que el chef (Spring) sigue para preparar un plato. Cada vez que un cliente (tu aplicación) pide ese plato, el chef usa la misma receta.
- **Alcance Singleton**: Es como un plato que se prepara una sola vez y se sirve a todos los clientes que lo piden. Todos reciben la misma porción.
- **Alcance Prototype**: Es como un plato que se prepara desde cero cada vez que un cliente lo pide. Cada cliente recibe una porción nueva.

En este caso, `CommentService` es un plato que se prepara una sola vez (Singleton) y se sirve a todos los clientes que lo solicitan.

---

### **Conclusión**
Declarar un bean con alcance Singleton usando la anotación `@Bean` en Spring es una forma de decirle a Spring que cree y gestione una única instancia de un objeto en toda la aplicación. Esto es útil para objetos que deben ser compartidos y reutilizados en diferentes partes de la aplicación. La anotación `@Bean` te permite definir cómo se crea el bean, y Spring se encarga de gestionar su ciclo de vida. Este enfoque respeta principios de diseño como la separación de responsabilidades y la modularidad, lo que facilita la creación de aplicaciones escalables y mantenibles.