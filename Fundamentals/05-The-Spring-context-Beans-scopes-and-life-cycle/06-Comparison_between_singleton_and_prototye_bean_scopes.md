Para explicar las diferencias entre los alcances **singleton** y **prototype** en Spring, utilizaremos el enfoque de **primeros principios**, desglosando el tema en pasos claros y sencillos. Esta explicación está dirigida a personas con conocimientos básicos de programación pero que son nuevas en Spring.

---

### **1. ¿Qué es un 'bean' en Spring y por qué es importante su alcance?**

#### **Definición de bean**:
- En Spring, un **bean** es un objeto que es gestionado por el **contenedor de Spring**. Estos objetos son creados, configurados y gestionados por Spring, lo que facilita su uso en la aplicación.
- Los beans son los componentes básicos de una aplicación Spring. Pueden ser servicios, repositorios, configuraciones, o cualquier otro objeto que necesites en tu aplicación.

#### **Importancia del alcance**:
- El **alcance** de un bean define cómo y cuándo se crean y gestionan las instancias del bean.
- El alcance afecta el comportamiento del bean en términos de **creación de instancias**, **uso de memoria** y **compartición de estado**.

**Ejemplo práctico**: Piensa en un bean como una "herramienta" que Spring te da para construir una aplicación. El alcance define si todos usan la misma herramienta (singleton) o si cada uno recibe una herramienta nueva (prototype).

---

### **2. Concepto de **singleton****

#### **¿Cómo gestiona Spring las instancias de beans singleton?**
- En el alcance **singleton**, Spring crea **una única instancia** del bean y la reutiliza en toda la aplicación.
- Esta instancia única se comparte entre todos los componentes que la solicitan.

#### **Comportamiento predeterminado**:
- El alcance singleton es el **comportamiento predeterminado** en Spring. Si no especificas un alcance, Spring asume que el bean es singleton.

#### **¿Por qué no se recomienda que los beans singleton tengan atributos mutables?**
- Los beans singleton son compartidos en toda la aplicación. Si un bean singleton tiene atributos mutables (es decir, atributos que pueden cambiar), cualquier cambio en esos atributos afectará a todos los componentes que usen el bean.
- Esto puede llevar a **problemas de concurrencia** y **comportamiento inesperado** en aplicaciones multi-hilo.

**Ejemplo práctico**: Imagina que un bean singleton es como una "pizarra compartida" en una oficina. Si alguien escribe en la pizarra, todos los demás verán el cambio. Esto puede ser problemático si varias personas intentan escribir al mismo tiempo.

---

### **3. Concepto de **prototype****

#### **¿Cómo gestiona Spring las instancias de beans prototype?**
- En el alcance **prototype**, Spring crea una **nueva instancia** del bean cada vez que se solicita.
- Cada componente que solicita el bean recibe una instancia independiente.

#### **Diferencias con singleton**:
- **Singleton**: Una única instancia compartida en toda la aplicación.
- **Prototype**: Una nueva instancia cada vez que se solicita el bean.

#### **¿Por qué los beans prototype pueden tener atributos mutables?**
- Como cada componente recibe su propia instancia del bean, los cambios en los atributos de un bean prototype no afectan a otros componentes.
- Esto hace que los beans prototype sean ideales para situaciones donde el **estado del bean** puede cambiar o donde se necesita **aislamiento** entre componentes.

**Ejemplo práctico**: Imagina que un bean prototype es como un "cuaderno personal". Cada persona recibe su propio cuaderno, y lo que escriben en él no afecta a los demás.

---

### **4. Comparación entre singleton y prototype**

#### **Creación de instancias**:
- **Singleton**: La instancia se crea una vez cuando se carga el contexto de Spring y se reutiliza en toda la aplicación.
- **Prototype**: Una nueva instancia se crea cada vez que se solicita el bean.

#### **Uso de memoria y rendimiento**:
- **Singleton**: Ahorra memoria porque solo hay una instancia, pero puede generar problemas de concurrencia si el estado del bean es mutable.
- **Prototype**: Consume más memoria porque se crean múltiples instancias, pero es más seguro en aplicaciones multi-hilo o cuando el estado del bean es mutable.

#### **Casos de uso recomendados**:
- **Singleton**: Ideal para beans que no tienen estado mutable o que son de solo lectura, como servicios de configuración o utilidades.
- **Prototype**: Ideal para beans que tienen estado mutable o que se usan en contextos concurrentes, como procesadores de solicitudes HTTP o beans que manejan datos específicos de una operación.

---

### **5. Ejemplo práctico**

#### **Configuración de un bean singleton**:
```java
@Service // Alcance singleton por defecto
public class CommentService {
    private String message = "Hello";

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
```

#### **Configuración de un bean prototype**:
```java
@Service
@Scope("prototype")
public class CommentRepository {
    private String data = "Default Data";

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
```

#### **Uso de los beans en la clase `Main`**:
```java
public class Main {
    public static void main(String[] args) {
        var c = new AnnotationConfigApplicationContext(ProjectConfig.class);

        // Singleton: Misma instancia
        var service1 = c.getBean(CommentService.class);
        var service2 = c.getBean(CommentService.class);
        service1.setMessage("Updated Message");
        System.out.println(service2.getMessage()); // Imprime "Updated Message"

        // Prototype: Diferentes instancias
        var repo1 = c.getBean(CommentRepository.class);
        var repo2 = c.getBean(CommentRepository.class);
        repo1.setData("New Data");
        System.out.println(repo2.getData()); // Imprime "Default Data"
    }
}
```

#### **Salida en la consola**:
```
Updated Message
Default Data
```

- **Explicación**:
    - **Singleton**: `service1` y `service2` comparten la misma instancia, por lo que el cambio en `service1` afecta a `service2`.
    - **Prototype**: `repo1` y `repo2` son instancias diferentes, por lo que el cambio en `repo1` no afecta a `repo2`.

---

### **6. Analogía para entender cuándo y por qué usar cada alcance**

Imagina que estás en una oficina:

- **Singleton**: Es como una "impresora compartida". Todos los empleados usan la misma impresora, lo que ahorra recursos, pero puede haber problemas si varias personas intentan imprimir al mismo tiempo.
- **Prototype**: Es como un "cuaderno personal". Cada empleado recibe su propio cuaderno, lo que evita conflictos, pero consume más recursos.

---

### **Conclusión**

El alcance **singleton** es ideal para beans que no tienen estado mutable o que son de solo lectura, ya que ahorra memoria y recursos. Sin embargo, no es recomendable para beans con estado mutable en aplicaciones concurrentes. Por otro lado, el alcance **prototype** es perfecto para beans que tienen estado mutable o que se usan en contextos concurrentes, ya que cada componente recibe su propia instancia, lo que garantiza aislamiento y seguridad. Elegir el alcance correcto depende de las necesidades específicas de la aplicación y del comportamiento deseado de los beans.