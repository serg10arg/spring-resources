### **Análisis del Error**

El código proporcionado tiene un problema relacionado con **la inyección de dependencias de Spring y la ambigüedad en los beans configurados**. Vamos a desglosar el problema y proporcionar una solución.

---

### **Origen del Error**

1. **Clases `Parrot` y Beans Duplicados**:
    - En la clase `ProjectConfig`, se definen **dos beans** de tipo `Parrot` con nombres diferentes (`parrot1` y `parrot2`).
    - Spring intenta inyectar un bean de tipo `Parrot` en el constructor de la clase `Person`, pero como existen dos beans candidatos (`parrot1` y `parrot2`), se produce una ambigüedad.

2. **Clase `Person`**:
    - La clase `Person` tiene un constructor que requiere un objeto `Parrot` (`public Person(Parrot parrot2)`).
    - Al no especificar cuál bean `Parrot` debe ser inyectado, Spring no sabe cuál usar.

3. **Error de Ejecución**:
    - Este escenario genera una excepción de tipo:
      ```
      NoUniqueBeanDefinitionException: No qualifying bean of type 'beans.Parrot' available: expected single matching bean but found 2: parrot1,parrot2
      ```
    - Esto ocurre porque Spring requiere una forma de decidir qué bean `Parrot` inyectar, pero no se ha proporcionado.

---

### **Solución del Problema**

#### **Solución 1: Usar el Nombre del Bean con `@Qualifier`**
Indica explícitamente cuál bean `Parrot` debe ser inyectado en el constructor de `Person`.

- Modificación en la clase `Person`:
  ```java
  import org.springframework.beans.factory.annotation.Qualifier;
  import org.springframework.stereotype.Component;

  @Component
  public class Person {

      private String name = "Ella";
      private final Parrot parrot;

      public Person(@Qualifier("parrot1") Parrot parrot) { // Indicar el bean específico
          this.parrot = parrot;
      }

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }

      public Parrot getParrot() {
          return parrot;
      }
  }
  ```

#### **Solución 2: Usar la Anotación `@Primary`**
Marca uno de los beans `Parrot` como el predeterminado para resolver conflictos.

- Modificación en `ProjectConfig`:
  ```java
  @Bean
  @Primary
  public Parrot parrot1() {
      Parrot p = new Parrot();
      p.setName("Koko");
      return p;
  }

  @Bean
  public Parrot parrot2() {
      Parrot p = new Parrot();
      p.setName("Miki");
      return p;
  }
  ```
  En este caso, `parrot1` será el bean predeterminado, y se inyectará automáticamente en `Person`.

#### **Solución 3: Usar la Anotación `@Autowired` con `@Qualifier`**
Se puede combinar `@Autowired` y `@Qualifier` para mayor claridad.

- Modificación en la clase `Person`:
  ```java
  @Component
  public class Person {

      private String name = "Ella";

      private Parrot parrot;

      @Autowired
      public void setParrot(@Qualifier("parrot1") Parrot parrot) {
          this.parrot = parrot;
      }

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }

      public Parrot getParrot() {
          return parrot;
      }
  }
  ```

---

### **Explicación del Flujo Corregido**

1. **Definición de Beans (`ProjectConfig`)**:
    - Ahora, uno de los beans (`parrot1`) es explícitamente designado como el predeterminado (usando `@Primary`) o seleccionado directamente con `@Qualifier`.

2. **Inyección de Dependencias en `Person`**:
    - El constructor o método setter de `Person` utiliza `@Qualifier` para especificar cuál de los dos beans `Parrot` debe ser inyectado.

3. **Ejecución**:
    - Al ejecutar el método `main` en la clase `Main`, Spring resolverá correctamente la dependencia de `Parrot` y no lanzará excepciones.

---

### **Resultado Esperado**

Con cualquiera de las soluciones implementadas, el programa imprimirá:

```
Person's name: Ella
Person's parrot: Parrot : Koko
```

Esto indica que el bean `parrot1` fue inyectado exitosamente en `Person`.