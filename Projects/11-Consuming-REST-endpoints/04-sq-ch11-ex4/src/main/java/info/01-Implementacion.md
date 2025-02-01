
---

### **Objetivo del programa**
El objetivo del programa es recibir una solicitud HTTP POST en un endpoint `/payment`, enviar esa solicitud a un servicio externo utilizando **WebClient** (una herramienta reactiva para hacer llamadas HTTP), y devolver la respuesta al cliente.

---

### **Descomposición en primeros principios**
1. **¿Qué es una aplicación Spring Boot?**
    - Es un framework que facilita la creación de aplicaciones Java basadas en Spring. Proporciona configuraciones automáticas y un entorno listo para ejecutar aplicaciones web.

2. **¿Qué es un controlador REST?**
    - Es un componente que maneja las solicitudes HTTP entrantes y devuelve respuestas. En este caso, el controlador recibe una solicitud POST en el endpoint `/payment`.

3. **¿Qué es un modelo de datos?**
    - Es una clase que representa la estructura de los datos que se envían y reciben. Aquí, la clase `Payment` representa un pago con un `id` y un `amount`.

4. **¿Qué es un proxy?**
    - Es un intermediario que realiza operaciones en nombre de otro componente. En este caso, el `PaymentsProxy` realiza llamadas HTTP al servicio externo.

5. **¿Qué es WebClient?**
    - Es una herramienta reactiva de Spring para realizar llamadas HTTP de manera no bloqueante. Es ideal para aplicaciones que requieren alta concurrencia y escalabilidad.

6. **¿Qué es la programación reactiva?**
    - Es un paradigma de programación que maneja flujos de datos asíncronos y no bloqueantes. En este programa, se utiliza `Mono` para manejar operaciones asíncronas.

---

### **Reconstrucción del flujo del programa**
Ahora que hemos descompuesto el problema en sus componentes básicos, reconstruiremos el flujo del programa paso a paso, entendiendo cómo las clases se integran entre sí.

---

#### **1. Clase `Main` (Punto de entrada de la aplicación)**
```java
@SpringBootApplication
public class Main {
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}
```
- **Propósito**: Iniciar la aplicación Spring Boot.
- **Detalle**:
    - La anotación `@SpringBootApplication` habilita la configuración automática y el escaneo de componentes.
    - El método `main` inicia la aplicación, cargando todas las configuraciones y componentes.

---

#### **2. Clase `ProjectConfig` (Configuración del proyecto)**
```java
@Configuration
public class ProjectConfig {
  @Bean
  public WebClient webClient() {
    return WebClient.builder().build();
  }
}
```
- **Propósito**: Configurar y proporcionar una instancia de `WebClient`.
- **Detalle**:
    - La anotación `@Configuration` indica que esta clase es una clase de configuración de Spring.
    - El método `webClient()` está anotado con `@Bean`, lo que permite que Spring lo gestione como un bean. Este método devuelve una instancia de `WebClient`, que se utilizará para realizar llamadas HTTP.

---

#### **3. Clase `Payment` (Modelo de datos)**
```java
public class Payment {
  private String id;
  private double amount;

  // Getters y setters
}
```
- **Propósito**: Representar los datos de un pago.
- **Detalle**:
    - Contiene dos atributos: `id` y `amount`.
    - Se utiliza para serializar y deserializar los datos en las solicitudes y respuestas HTTP.

---

#### **4. Clase `PaymentsProxy` (Proxy para llamadas HTTP)**
```java
@Component
public class PaymentsProxy {
  private final WebClient webClient;

  @Value("${name.service.url}")
  private String url;

  public PaymentsProxy(WebClient webClient) {
    this.webClient = webClient;
  }

  public Mono<Payment> createPayment(String requestId, Payment payment) {
    return webClient.post()
              .uri(url + "/payment")
              .header("requestId", requestId)
              .body(Mono.just(payment), Payment.class)
              .retrieve()
              .bodyToMono(Payment.class);
  }
}
```
- **Propósito**: Realizar llamadas HTTP al servicio externo utilizando `WebClient`.
- **Detalle**:
    - La anotación `@Component` indica que esta clase es un componente de Spring y se gestionará como un bean.
    - El campo `url` se inyecta con el valor de la propiedad `name.service.url` (definida en `application.properties`).
    - El constructor recibe una instancia de `WebClient` (inyectada por Spring) y la asigna al campo `webClient`.
    - El método `createPayment`:
        1. Construye la URL completa del servicio externo.
        2. Agrega un encabezado `requestId` a la solicitud.
        3. Envía el cuerpo de la solicitud (un objeto `Payment`) utilizando `body(Mono.just(payment), Payment.class)`.
        4. Realiza la solicitud HTTP POST utilizando `retrieve()`.
        5. Convierte la respuesta en un `Mono<Payment>` utilizando `bodyToMono(Payment.class)`.

---

#### **5. Clase `PaymentsController` (Controlador REST)**
```java
@RestController
public class PaymentsController {
  private final PaymentsProxy paymentsProxy;

  public PaymentsController(PaymentsProxy paymentsProxy) {
    this.paymentsProxy = paymentsProxy;
  }

  @PostMapping("/payment")
  public Mono<Payment> createPayment(@RequestBody Payment payment) {
    String requestId = UUID.randomUUID().toString();
    return paymentsProxy.createPayment(requestId, payment);
  }
}
```
- **Propósito**: Manejar las solicitudes HTTP relacionadas con pagos.
- **Detalle**:
    - La anotación `@RestController` indica que esta clase es un controlador REST.
    - El campo `paymentsProxy` es una instancia de `PaymentsProxy` que se inyecta automáticamente por Spring.
    - El método `createPayment`:
        1. Recibe un objeto `Payment` en el cuerpo de la solicitud.
        2. Genera un `requestId` único utilizando `UUID.randomUUID().toString()`.
        3. Llama al método `createPayment` del `PaymentsProxy` para enviar la solicitud al servicio externo.
        4. Devuelve un `Mono<Payment>`, que representa la respuesta asíncrona del servicio externo.

---

#### **6. Archivo de configuración (`application.properties`)**
```properties
server.port=9090
name.service.url=http://localhost:8080
```
- **Propósito**: Configurar propiedades específicas de la aplicación.
- **Detalle**:
    - `server.port=9090`: Define el puerto en el que la aplicación Spring Boot escuchará las solicitudes entrantes.
    - `name.service.url=http://localhost:8080`: Define la URL base del servicio externo.

---

### **Integración de las clases**
1. **Inicio de la aplicación**: La clase `Main` inicia la aplicación Spring Boot.
2. **Configuración de WebClient**: La clase `ProjectConfig` proporciona una instancia de `WebClient`.
3. **Proxy para llamadas HTTP**: La clase `PaymentsProxy` utiliza `WebClient` para realizar llamadas HTTP al servicio externo.
4. **Controlador REST**: La clase `PaymentsController` recibe solicitudes HTTP en el endpoint `/payment` y utiliza el `PaymentsProxy` para enviar la solicitud al servicio externo.
5. **Modelo de datos**: La clase `Payment` se utiliza para representar los datos de un pago en las solicitudes y respuestas HTTP.

---

### **Flujo del programa**
1. Un cliente realiza una solicitud POST al endpoint `/payment` de la aplicación Spring Boot (que está escuchando en el puerto 9090).
2. El controlador `PaymentsController` recibe la solicitud, genera un `requestId` único y utiliza el `PaymentsProxy` para enviar la solicitud al servicio externo.
3. El `PaymentsProxy`:
    - Construye la URL completa del servicio externo.
    - Agrega el `requestId` en los encabezados.
    - Envía el objeto `Payment` en el cuerpo de la solicitud.
    - Realiza la solicitud HTTP POST utilizando `WebClient`.
4. El servicio externo procesa la solicitud y devuelve una respuesta, que el controlador luego devuelve al cliente.

---

### **Conclusión**
El programa utiliza un enfoque reactivo para manejar solicitudes HTTP de manera eficiente y no bloqueante. Cada clase tiene una responsabilidad clara, y la integración entre ellas sigue las mejores prácticas de Spring Boot y la programación reactiva. Al descomponer el problema en primeros principios, podemos entender claramente cómo cada componente contribuye al objetivo final del programa.