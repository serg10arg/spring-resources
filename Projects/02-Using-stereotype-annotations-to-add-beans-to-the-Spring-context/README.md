### Explicación utilizando el enfoque de primeros principios

Se detalla cómo usar **anotaciones estereotípicas** en Spring para añadir objetos (beans) al contexto de Spring. Para desglosarlo desde primeros principios, vamos a analizar los fundamentos básicos que sustentan estas ideas y cómo se relacionan.

---

#### **Primera idea fundamental: ¿Qué es el contexto de Spring?**
1. **Principio:** El **contexto de Spring** es un contenedor que administra los objetos (beans) de una aplicación, asegurándose de que se creen, configuren y gestionen adecuadamente durante el ciclo de vida de la aplicación.
    - **Ejemplo:** Piensa en un directorio telefónico. Es un lugar donde puedes buscar un contacto (objeto) sin preocuparte por dónde se encuentra físicamente el contacto. Spring actúa como este directorio para los objetos.

2. **Relación:** Los beans son los componentes esenciales que Spring gestiona dentro de este contexto.

---

#### **Segunda idea fundamental: ¿Por qué necesitamos añadir beans al contexto?**
1. **Principio:** Añadir beans al contexto permite que Spring sea consciente de ellos y pueda gestionarlos automáticamente (como inyección de dependencias, inicialización, etc.).
    - **Ejemplo:** Si tienes un robot (Spring) que organiza tu casa, primero necesitas decirle qué cosas hay (los objetos). El robot no puede organizar lo que no conoce.

2. **Relación:** El proceso de añadir beans al contexto define qué objetos estarán disponibles y cómo interactuarán en la aplicación.

---

#### **Tercera idea fundamental: ¿Qué son las anotaciones estereotípicas?**
1. **Principio:** Las anotaciones estereotípicas (como `@Component`) son instrucciones que se añaden al código para indicarle a Spring que debe incluir una clase específica en su contexto como bean.
    - **Ejemplo práctico:** Imagina que marcas con un adhesivo especial los muebles que quieres que el robot limpie. El adhesivo indica que ese objeto debe ser considerado.

2. **Relación:** Estas anotaciones simplifican el proceso de configuración, permitiendo menos código manual y más automatización.

---

#### **Desglose de los pasos básicos descritos en el texto**
1. **Paso 1: Marcar la clase con `@Component`.**
    - **Fundamento:** Al usar esta anotación, le dices a Spring que debe crear una instancia de esta clase y añadirla a su contexto.
    - **Ejemplo:** Si tienes una clase `Parrot`, marcarla con `@Component` sería como etiquetar un objeto real (un loro) para que sea gestionado por el sistema.

2. **Paso 2: Usar `@ComponentScan` para indicar dónde buscar las clases anotadas.**
    - **Fundamento:** Spring no busca automáticamente las clases marcadas con `@Component`. Con `@ComponentScan`, especificas qué paquetes inspeccionar.
    - **Ejemplo:** Imagina que el robot necesita saber en qué habitaciones buscar los muebles con adhesivos. `@ComponentScan` define estas habitaciones.

---

#### **Ejemplo práctico del flujo**
- **Definir una clase `Parrot`:**
   ```java
   @Component
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
  **Análisis:** Aquí, `@Component` indica que esta clase debe ser gestionada por Spring.

- **Crear una configuración que use `@ComponentScan`:**
   ```java
   @Configuration
   @ComponentScan(basePackages = "main")
   public class ProjectConfig {
   }
   ```
  **Análisis:** Esta configuración indica que Spring debe buscar en el paquete `main` las clases anotadas con `@Component`.

- **Probar en el método `main`:**
   ```java
   public class Main {
       public static void main(String[] args) {
           var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
           Parrot p = context.getBean(Parrot.class);
           System.out.println(p);         // Imprime la representación del objeto
           System.out.println(p.getName()); // Imprime `null` (no se asignó un nombre)
       }
   }
   ```
  **Análisis:** Spring crea una instancia de `Parrot`, pero no modifica sus valores iniciales a menos que lo especifiques.

---

#### **Comparación de enfoques (usando `@Component` vs `@Bean`)**
- **`@Component`:** Útil para clases que controlas directamente. Es más simple y requiere menos código.
- **`@Bean`:** Útil para clases externas o cuando necesitas más personalización en cómo se crean las instancias.

---

### **Conclusión**
El uso de anotaciones estereotípicas como `@Component` es una forma declarativa y eficiente de registrar beans en el contexto de Spring. Al marcar las clases con estas anotaciones y especificar dónde buscarlas con `@ComponentScan`, se automatiza gran parte del proceso. Sin embargo, la elección entre `@Component` y `@Bean` depende del nivel de control que necesites sobre la creación de instancias.