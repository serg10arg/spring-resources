
---

### **1. ¿Qué es un servicio en Spring?**

En Spring, un **servicio** es una clase que contiene la lógica de negocio de tu aplicación. Es el "cerebro" que decide qué hacer con los datos y cómo manipularlos. Los servicios suelen estar marcados con la anotación `@Service`, que le dice a Spring: "Esta clase es responsable de manejar la lógica de negocio".

---

### **2. ¿Qué hace esta clase `TransferService`?**

La clase `TransferService` tiene dos métodos principales:

1. **`transferMoney`**: Realiza una transferencia de dinero entre dos cuentas.
2. **`getAllAccounts`**: Obtiene una lista de todas las cuentas.

Vamos a analizar cada uno de estos métodos y cómo funcionan.

---

### **3. Componentes clave de la clase**

#### **a) `AccountRepository`**

`AccountRepository` es una clase que interactúa con la base de datos. Su trabajo es obtener y actualizar datos de las cuentas.

- **¿Qué hace?**:
    - Busca cuentas por su ID.
    - Actualiza el saldo de las cuentas.
    - Obtiene todas las cuentas.

- **¿Cómo se usa?**:
    - Se pasa como parámetro en el constructor de `TransferService` (esto se llama **inyección de dependencias**).
    - Se utiliza en los métodos del servicio para interactuar con la base de datos.

#### **b) Anotaciones**

- **`@Service`**: Indica que esta clase es un servicio y que Spring debe gestionarla.
- **`@Transactional`**: Indica que el método debe ejecutarse dentro de una transacción. Si algo sale mal, todos los cambios se revertirán.

---

### **4. Métodos de la clase**

#### **a) `transferMoney`**

- **¿Qué hace?**:
    - Realiza una transferencia de dinero entre dos cuentas.
    - Asegura que la operación sea atómica (es decir, que todas las operaciones se completen o ninguna).

- **Desglose del método**:
    1. **Obtener las cuentas**:
        - `Account sender = accountRepository.findAccountById(idSender);`
        - `Account receiver = accountRepository.findAccountById(idReceiver);`
        - Busca las cuentas del remitente y del receptor en la base de datos.

    2. **Calcular los nuevos saldos**:
        - `BigDecimal senderNewAmount = sender.getAmount().subtract(amount);`
        - `BigDecimal receiverNewAmount = receiver.getAmount().add(amount);`
        - Resta el monto de la cuenta del remitente y lo suma a la cuenta del receptor.

    3. **Actualizar los saldos en la base de datos**:
        - `accountRepository.changeAmount(idSender, senderNewAmount);`
        - `accountRepository.changeAmount(idReceiver, receiverNewAmount);`
        - Actualiza los saldos de ambas cuentas en la base de datos.

- **¿Por qué es importante `@Transactional`?**:
    - Si algo sale mal durante la transferencia (por ejemplo, no hay suficiente saldo o la base de datos falla), Spring revertirá todos los cambios.
    - Esto asegura que los datos estén siempre en un estado consistente.

#### **b) `getAllAccounts`**

- **¿Qué hace?**:
    - Obtiene una lista de todas las cuentas de la base de datos.

- **Desglose del método**:
    - `return accountRepository.findAllAccounts();`
    - Llama al repositorio para obtener todas las cuentas y las devuelve.

---

### **5. Relación con otras clases**

#### **a) `Account`**:
- Es el modelo de datos que representa una cuenta bancaria.
- `TransferService` usa objetos `Account` para realizar transferencias.

#### **b) `AccountRepository`**:
- Es la clase que interactúa con la base de datos.
- `TransferService` usa `AccountRepository` para obtener y actualizar cuentas.

#### **c) `AccountController`**:
- Es el controlador que maneja las solicitudes HTTP.
- Llama a `TransferService` para realizar transferencias y obtener cuentas.

---

### **6. Ejemplo de flujo completo**

1. **Transferencia de dinero**:
    - Un cliente envía una solicitud POST a `/transfer` con los IDs de las cuentas y el monto a transferir.
    - El controlador llama a `transferService.transferMoney(...)`.
    - El servicio obtiene las cuentas, calcula los nuevos saldos y actualiza la base de datos.
    - Si todo va bien, la transferencia se realiza. Si algo falla, Spring revierte los cambios.

2. **Obtener todas las cuentas**:
    - Un cliente envía una solicitud GET a `/accounts`.
    - El controlador llama a `transferService.getAllAccounts()`.
    - El servicio obtiene todas las cuentas de la base de datos y las devuelve.

---

### **7. ¿Por qué es importante `@Transactional`?**

Imagina que estás realizando una transferencia y ocurre un error después de restar el monto de la cuenta del remitente pero antes de sumarlo a la cuenta del receptor. Sin `@Transactional`, el saldo del remitente se reduciría, pero el saldo del receptor no aumentaría, lo que resultaría en una inconsistencia en los datos.

Con `@Transactional`, si algo sale mal, Spring asegura que **ninguno** de los cambios se guarde en la base de datos. Esto mantiene los datos consistentes.

---

### **8. Resumen**

- **`TransferService`** es una clase que contiene la lógica de negocio para transferir dinero y obtener cuentas.
- Usa **`AccountRepository`** para interactuar con la base de datos.
- El método `transferMoney` está anotado con `@Transactional`, lo que asegura que la transferencia sea atómica.
- El método `getAllAccounts` simplemente obtiene todas las cuentas de la base de datos.

---

### **9. Ejemplo de código en `AccountController`**

Aquí hay un ejemplo de cómo se usa `TransferService` en `AccountController`:

```java
@RestController
public class AccountController {

    private final TransferService transferService;

    public AccountController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public void transferMoney(@RequestBody TransferRequest request) {
        transferService.transferMoney(
                request.getSenderAccountId(),
                request.getReceiverAccountId(),
                request.getAmount());
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return transferService.getAllAccounts();
    }
}
```

- **`transferMoney`**: Llama al servicio para realizar una transferencia.
- **`getAllAccounts`**: Llama al servicio para obtener todas las cuentas.

---

