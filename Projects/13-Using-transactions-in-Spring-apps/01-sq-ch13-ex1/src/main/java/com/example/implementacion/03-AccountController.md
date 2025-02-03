
---

### **1. ¿Qué es un controlador en Spring?**

En Spring, un **controlador** es una clase que maneja las solicitudes HTTP y devuelve respuestas. Es como el "recepcionista" de tu aplicación: recibe las solicitudes de los clientes (por ejemplo, un navegador web o Postman), decide qué hacer con ellas y devuelve una respuesta.

La anotación `@RestController` le dice a Spring que esta clase es un controlador y que las respuestas que devuelve serán en formato JSON (un formato común para intercambiar datos en aplicaciones web).

---

### **2. ¿Qué hace esta clase `AccountController`?**

La clase `AccountController` tiene dos métodos principales:

1. **`transferMoney`**: Maneja las solicitudes POST para transferir dinero entre cuentas.
2. **`getAllAccounts`**: Maneja las solicitudes GET para obtener todas las cuentas.

Vamos a analizar cada uno de estos métodos y cómo funcionan.

---

### **3. Componentes clave de la clase**

#### **a) `TransferService`**

`TransferService` es una clase que contiene la lógica de negocio para realizar transferencias y obtener cuentas. El controlador no hace el trabajo pesado; en su lugar, delega estas tareas al servicio.

- **¿Qué hace?**:
    - Realiza transferencias de dinero.
    - Obtiene la lista de todas las cuentas.

- **¿Cómo se usa?**:
    - Se pasa como parámetro en el constructor de `AccountController` (esto se llama **inyección de dependencias**).
    - Se utiliza en los métodos del controlador para realizar las operaciones necesarias.

#### **b) Anotaciones**

- **`@RestController`**: Indica que esta clase es un controlador y que las respuestas serán en formato JSON.
- **`@PostMapping`**: Indica que el método maneja solicitudes POST.
- **`@GetMapping`**: Indica que el método maneja solicitudes GET.
- **`@RequestBody`**: Indica que el cuerpo de la solicitud HTTP debe convertirse en un objeto Java (en este caso, `TransferRequest`).

---

### **4. Métodos de la clase**

#### **a) `transferMoney`**

- **¿Qué hace?**:
    - Maneja las solicitudes POST para transferir dinero entre cuentas.
    - Recibe un objeto `TransferRequest` en el cuerpo de la solicitud.
    - Llama al servicio para realizar la transferencia.

- **Desglose de la sintaxis**:
    - **`@PostMapping("/transfer")`**: Indica que este método maneja solicitudes POST a la URL `/transfer`.
    - **`@RequestBody TransferRequest request`**: Indica que el cuerpo de la solicitud debe convertirse en un objeto `TransferRequest`.
    - **`transferService.transferMoney(...)`**: Llama al servicio para realizar la transferencia.

- **Relación con otras clases**:
    - **`TransferRequest`**: Es un objeto que contiene los datos necesarios para la transferencia (IDs de las cuentas y el monto).
    - **`TransferService`**: Es el servicio que realiza la transferencia.

- **Ejemplo**:
    - Si un cliente envía una solicitud POST a `/transfer` con el siguiente cuerpo:
      ```json
      {
        "senderAccountId": 1,
        "receiverAccountId": 2,
        "amount": 100
      }
      ```
    - El controlador convierte este JSON en un objeto `TransferRequest`.
    - Luego, llama a `transferService.transferMoney(1, 2, 100)` para realizar la transferencia.

#### **b) `getAllAccounts`**

- **¿Qué hace?**:
    - Maneja las solicitudes GET para obtener todas las cuentas.
    - Devuelve una lista de objetos `Account`.

- **Desglose de la sintaxis**:
    - **`@GetMapping("/accounts")`**: Indica que este método maneja solicitudes GET a la URL `/accounts`.
    - **`transferService.getAllAccounts()`**: Llama al servicio para obtener todas las cuentas.

- **Relación con otras clases**:
    - **`Account`**: Es el modelo de datos que representa una cuenta bancaria.
    - **`TransferService`**: Es el servicio que obtiene la lista de cuentas.

- **Ejemplo**:
    - Si un cliente envía una solicitud GET a `/accounts`, el controlador llama a `transferService.getAllAccounts()`.
    - El servicio devuelve una lista de objetos `Account`, que el controlador convierte en JSON y devuelve como respuesta.

---

### **5. Resumen**

- **`AccountController`** es un controlador que maneja las solicitudes HTTP relacionadas con cuentas bancarias.
- Usa **`TransferService`** para realizar las operaciones de negocio (transferencias y obtención de cuentas).
- Expone dos endpoints:
    - **POST `/transfer`**: Para transferir dinero entre cuentas.
    - **GET `/accounts`**: Para obtener todas las cuentas.
- Las respuestas se devuelven en formato JSON.

---

### **6. Ejemplo de flujo completo**

1. **Transferencia de dinero**:
    - Un cliente envía una solicitud POST a `/transfer` con un cuerpo JSON.
    - El controlador convierte el JSON en un objeto `TransferRequest`.
    - El controlador llama a `transferService.transferMoney(...)` para realizar la transferencia.
    - Si todo va bien, la transferencia se realiza y no se devuelve ningún contenido en la respuesta.

2. **Obtener todas las cuentas**:
    - Un cliente envía una solicitud GET a `/accounts`.
    - El controlador llama a `transferService.getAllAccounts()` para obtener la lista de cuentas.
    - El controlador devuelve la lista como respuesta en formato JSON.

---

### **7. ¿Cómo probar en Postman?**

#### **a) Transferencia de dinero**

1. **Método**: POST
2. **URL**: `http://localhost:8080/transfer`
3. **Body (JSON)**:
   ```json
   {
     "senderAccountId": 1,
     "receiverAccountId": 2,
     "amount": 100
   }
   ```
4. **Respuesta**: Si la transferencia es exitosa, no recibirás ningún contenido en la respuesta.

#### **b) Obtener todas las cuentas**

1. **Método**: GET
2. **URL**: `http://localhost:8080/accounts`
3. **Respuesta**: Deberías recibir una lista de cuentas en formato JSON.

---

