Este programa es una aplicación Spring Boot que simula un sistema de procesamiento de pagos. La principal mejora respecto a versiones anteriores es la introducción de un **manejador global de excepciones** (`@RestControllerAdvice`), que centraliza el manejo de errores en lugar de hacerlo directamente en el controlador. A continuación, te explico paso a paso cómo se integran las clases para lograr la funcionalidad principal del programa:

---

### **1. Clase `Main` (Punto de entrada)**
- **Responsabilidad**: Inicia la aplicación Spring Boot.
- **Funcionamiento**:
    - La anotación `@SpringBootApplication` habilita la configuración automática y el escaneo de componentes.
    - El método `main` inicia la aplicación llamando a `SpringApplication.run`.

---

### **2. Clase `PaymentController` (Controlador REST)**
- **Responsabilidad**: Maneja las solicitudes HTTP relacionadas con pagos.
- **Funcionamiento**:
    - La anotación `@RestController` indica que esta clase es un controlador REST.
    - El constructor recibe una instancia de `PaymentService` (inyección de dependencias).
    - El método `makePayment` está mapeado a la ruta `/payment` con el método HTTP `POST`.
        - Llama al método `processPayment` del servicio para procesar el pago.
        - Si el pago es exitoso, devuelve un `ResponseEntity` con el estado `202 ACCEPTED` y los detalles del pago (`PaymentDetails`).
        - **Nota**: En este diseño, el controlador ya no maneja la excepción `NotEnoughMoneyException`, ya que esta responsabilidad se delega al manejador global de excepciones.

---

### **3. Clase `PaymentService` (Lógica de negocio)**
- **Responsabilidad**: Contiene la lógica para procesar pagos.
- **Funcionamiento**:
    - La anotación `@Service` la marca como un componente de servicio gestionado por Spring.
    - El método `processPayment` simula el procesamiento de un pago.
        - En este caso, siempre lanza una excepción `NotEnoughMoneyException` para simular un pago fallido.

---

### **4. Clase `NotEnoughMoneyException` (Excepción personalizada)**
- **Responsabilidad**: Representa un error cuando no hay suficiente dinero para procesar el pago.
- **Funcionamiento**:
    - Extiende `RuntimeException`, lo que la convierte en una excepción no verificada.
    - Se lanza en `PaymentService` cuando no se puede procesar el pago.

---

### **5. Clase `PaymentDetails` (Modelo de datos)**
- **Responsabilidad**: Representa los detalles de un pago exitoso.
- **Funcionamiento**:
    - Contiene un campo `amount` (monto del pago) con sus métodos getter y setter.
    - Se devuelve en la respuesta HTTP cuando el pago es exitoso.

---

### **6. Clase `ErrorDetails` (Modelo de datos)**
- **Responsabilidad**: Representa los detalles de un error.
- **Funcionamiento**:
    - Contiene un campo `message` (mensaje de error) con sus métodos getter y setter.
    - Se devuelve en la respuesta HTTP cuando ocurre un error.

---

### **7. Clase `ExceptionControllerAdvice` (Manejador global de excepciones)**
- **Responsabilidad**: Centraliza el manejo de excepciones en toda la aplicación.
- **Funcionamiento**:
    - La anotación `@RestControllerAdvice` indica que esta clase es un manejador global de excepciones.
    - El método `exceptionNotEnoughMoneyHandler` está anotado con `@ExceptionHandler` y se ejecuta cuando se lanza una excepción `NotEnoughMoneyException`.
        - Crea un objeto `ErrorDetails` con el mensaje "Not enough money to make the payment."
        - Devuelve un `ResponseEntity` con el estado `400 BAD REQUEST` y el objeto `ErrorDetails`.

---

### **Flujo de ejecución**
1. La aplicación se inicia desde la clase `Main`.
2. Spring Boot escanea y carga los componentes (`@RestController`, `@Service`, `@RestControllerAdvice`, etc.).
3. El cliente realiza una solicitud HTTP `POST` a la ruta `/payment`.
4. El método `makePayment` en `PaymentController` recibe la solicitud.
5. `PaymentController` llama al método `processPayment` de `PaymentService`.
6. `PaymentService` lanza una excepción `NotEnoughMoneyException`.
7. El manejador global de excepciones (`ExceptionControllerAdvice`) captura la excepción.
8. El método `exceptionNotEnoughMoneyHandler` construye una respuesta de error (`ErrorDetails`) con el mensaje "Not enough money to make the payment."
9. El cliente recibe una respuesta HTTP `400 BAD REQUEST` con el mensaje de error.

---

### **Resumen de integración**
- **Spring Boot**: Gestiona la aplicación y la configuración automática.
- **Controlador (`PaymentController`)**: Recibe solicitudes HTTP y delega la lógica de negocio al servicio.
- **Servicio (`PaymentService`)**: Contiene la lógica de negocio y lanza excepciones en caso de errores.
- **Manejador global de excepciones (`ExceptionControllerAdvice`)**: Centraliza el manejo de excepciones y construye respuestas de error.
- **Modelos (`PaymentDetails`, `ErrorDetails`)**: Representan los datos devueltos en las respuestas HTTP.
- **Excepción (`NotEnoughMoneyException`)**: Permite manejar errores específicos de la lógica de negocio.

---

### **Mejoras respecto al diseño anterior**
- **Manejo centralizado de excepciones**: En lugar de manejar las excepciones dentro del controlador, se utiliza un manejador global (`@RestControllerAdvice`). Esto hace que el código sea más limpio, mantenible y escalable.
- **Separación de responsabilidades**: El controlador se enfoca únicamente en manejar solicitudes HTTP, mientras que el manejador de excepciones se encarga de los errores.

---

### **Ejemplo de interacción**

#### **Solicitud HTTP**
```http
POST /payment HTTP/1.1
```

#### **Respuesta HTTP (en caso de error)**
```http
HTTP/1.1 400 BAD REQUEST
Content-Type: application/json

{
  "message": "Not enough money to make the payment"
}
```

---

### **Resumen**
Este programa es una aplicación Spring Boot que simula un sistema de procesamiento de pagos. El controlador (`PaymentController`) maneja las solicitudes HTTP, el servicio (`PaymentService`) contiene la lógica de negocio, y el manejador global de excepciones (`ExceptionControllerAdvice`) centraliza el manejo de errores. La clase `Main` inicia la aplicación.
