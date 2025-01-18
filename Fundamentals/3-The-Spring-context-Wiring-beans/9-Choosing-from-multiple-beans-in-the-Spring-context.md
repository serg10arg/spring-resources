### **Fundamentos básicos**

#### **1. ¿Qué es un bean en Spring?**
Un **bean** es un objeto administrado por el contenedor de Spring, que se encarga de instanciarlo, configurarlo y gestionarlo. Los beans representan los componentes principales de la aplicación.

---

#### **2. ¿Qué significa "múltiples beans"?**
En una aplicación, puedes tener varias instancias de un mismo tipo de objeto (o clase). Esto es útil cuando necesitas diferentes configuraciones o comportamientos del mismo tipo de componente. Por ejemplo:
- Dos **Parrot** (loros), cada uno con un nombre diferente: "Koko" y "Milo".

---

#### **3. Problema básico: Elegir entre múltiples beans**
Cuando hay más de un bean del mismo tipo, Spring no sabe cuál inyectar automáticamente. Esto puede llevar a errores como:
```java
NoUniqueBeanDefinitionException: Expected single matching bean but found 2
```

**Ejemplo práctico:**
Si tienes dos beans de tipo `Parrot` (definidos como "parrot1" y "parrot2"), Spring necesita una forma de saber cuál usar al inyectar en un componente.

---

### **Desglose del texto**

#### **1. Usar inyección de parámetros para múltiples beans**
Cuando un componente necesita un bean, Spring lo inyecta automáticamente. Pero si hay varios beans del mismo tipo, puedes:
- Usar el nombre del parámetro en el constructor.
- Especificar explícitamente qué bean inyectar.

**Ejemplo básico:**
```java
public class Person {
    private final Parrot parrot;

    @Autowired
    public Person(Parrot parrot) {
        this.parrot = parrot;
    }
}
```

Si hay múltiples beans de tipo `Parrot`, Spring no sabe cuál usar.

---

#### **2. Usar la anotación `@Qualifier`**
La anotación `@Qualifier` resuelve el problema de múltiples beans al especificar explícitamente cuál debe usarse.

**Definición:**
- `@Qualifier` indica a Spring qué bean específico debe inyectar.
- Esto se hace usando el nombre del bean.

**Ejemplo práctico:**
```java
@Component
public class Person {
    private final Parrot parrot;

    @Autowired
    public Person(@Qualifier("parrot1") Parrot parrot) {
        this.parrot = parrot;
    }
}
```

Aquí, Spring inyectará el bean llamado "parrot1".

---

#### **3. Definir múltiples beans en una clase de configuración**
En lugar de usar la anotación `@Component` para cada bean, puedes definirlos explícitamente en una clase de configuración usando `@Bean`.

**Ejemplo:**
```java
@Configuration
public class ParrotConfig {
    @Bean(name = "parrot1")
    public Parrot parrot1() {
        return new Parrot("Koko");
    }

    @Bean(name = "parrot2")
    public Parrot parrot2() {
        return new Parrot("Milo");
    }
}
```

Aquí, se definen dos beans de tipo `Parrot` con nombres únicos: "parrot1" y "parrot2".

---

### **Relación entre las ideas**

1. **Problema de múltiples beans:**
    - Spring no puede decidir automáticamente cuál inyectar cuando hay más de uno del mismo tipo.

2. **Solución: `@Qualifier`:**
    - Permite especificar el bean exacto que Spring debe usar.

3. **Definición explícita de beans:**
    - Usar una clase de configuración para crear y nombrar beans asegura claridad y control sobre los componentes.

---

### **Analogía práctica**

Imagina que tienes dos autos (beans) en tu garaje:
1. Un auto rojo llamado "auto1".
2. Un auto azul llamado "auto2".

Si le pides a alguien que saque un auto, no sabrá cuál elegir. Para resolver esto:
- Usas etiquetas claras: "rojo" o "azul" (equivalente a `@Qualifier`).
- O defines reglas específicas de uso en un documento (equivalente a una clase de configuración).

Esto asegura que siempre obtendrás el auto correcto para la tarea.

---

### **Conclusión**

1. **Múltiples beans:**
    - Son útiles para representar diferentes configuraciones o comportamientos de un mismo tipo de componente.

2. **Resolución del problema:**
    - Usa `@Qualifier` para especificar cuál bean debe ser inyectado.
    - Define los beans explícitamente en clases de configuración con nombres únicos.

3. **Ventajas de estas técnicas:**
    - Evitas errores como `NoUniqueBeanDefinitionException`.
    - Aumentas la claridad y el control en el manejo de dependencias.