### Análisis de las clases y su integración

Vamos a descomponer el programa en partes más pequeñas y entender cómo cada clase contribuye a la funcionalidad principal: **transferir dinero entre cuentas y consultar cuentas**. Esta explicación está dirigida a principiantes en Spring y Spring Data JDBC.

---

### 1. **Clase `Main` (Punto de entrada de la aplicación)**

```java
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

- **Qué hace:** Esta clase es el punto de entrada de la aplicación. La anotación `@SpringBootApplication` le dice a Spring que esta es una aplicación Spring Boot y que debe configurar automáticamente todo lo necesario.
- **Cómo se integra:** Al ejecutar esta clase, Spring Boot inicia la aplicación, carga las configuraciones y hace que los controladores, servicios y repositorios estén disponibles.

---

### 2. **Clase `Account` (Modelo de datos)**

```java
package com.example.model;

import org.springframework.data.annotation.Id;
import java.math.BigDecimal;

public class Account {

    @Id
    private long id;
    private String name;
    private BigDecimal amount;

    // Getters y setters
}
```

- **Qué hace:** Esta clase representa una **entidad** (en este caso, una cuenta bancaria). Tiene tres atributos: `id`, `name` y `amount`.
- **Cómo se integra:** Esta clase se mapea a una tabla en la base de datos. Spring Data JDBC usa esta clase para realizar operaciones como guardar, actualizar o consultar cuentas.

---

### 3. **Clase `AccountRepository` (Repositorio)**

```java
package com.example.repositories;

import com.example.model.Account;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("SELECT * FROM account WHERE name = :name")
    List<Account> findAccountsByName(String name);

    @Modifying
    @Query("UPDATE account SET amount = :amount WHERE id = :id")
    void changeAmount(long id, BigDecimal amount);
}
```

- **Qué hace:** Esta interfaz define cómo interactuar con la base de datos. Extiende `CrudRepository`, que proporciona métodos básicos como `save`, `findById`, `delete`, etc.
- **Métodos personalizados:**
    - `findAccountsByName`: Busca cuentas por nombre.
    - `changeAmount`: Actualiza el monto de una cuenta.
- **Cómo se integra:** Spring Data JDBC implementa automáticamente esta interfaz, por lo que no necesitas escribir código para ejecutar las consultas SQL.

---

### 4. **Clase `TransferService` (Lógica de negocio)**

```java
package com.example.services;

import com.example.exceptions.AccountNotFoundException;
import com.example.model.Account;
import com.example.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transferMoney(long idSender, long idReceiver, BigDecimal amount) {
        Account sender = accountRepository.findById(idSender)
                .orElseThrow(() -> new AccountNotFoundException());

        Account receiver = accountRepository.findById(idReceiver)
                .orElseThrow(() -> new AccountNotFoundException());

        BigDecimal senderNewAmount = sender.getAmount().subtract(amount);
        BigDecimal receiverNewAmount = receiver.getAmount().add(amount);

        accountRepository.changeAmount(idSender, senderNewAmount);
        accountRepository.changeAmount(idReceiver, receiverNewAmount);
    }

    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> findAccountsByName(String name) {
        return accountRepository.findAccountsByName(name);
    }
}
```

- **Qué hace:** Esta clase contiene la lógica de negocio para transferir dinero entre cuentas y consultar cuentas.
- **Métodos clave:**
    - `transferMoney`: Transfiere dinero de una cuenta a otra. Usa `@Transactional` para asegurarse de que la operación sea atómica (si algo falla, se revierte todo).
    - `getAllAccounts`: Obtiene todas las cuentas.
    - `findAccountsByName`: Busca cuentas por nombre.
- **Cómo se integra:** Esta clase usa `AccountRepository` para interactuar con la base de datos y proporciona métodos que el controlador puede llamar.

---

### 5. **Clase `AccountController` (Controlador)**

```java
package com.example.controllers;

import org.springframework.web.bind.annotation.*;
import com.example.dto.TransferRequest;
import com.example.model.Account;
import com.example.services.TransferService;

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
    public Iterable<Account> getAllAccounts(@RequestParam(required = false) String name) {
        if (name == null) {
            return transferService.getAllAccounts();
        } else {
            return transferService.findAccountsByName(name);
        }
    }
}
```

- **Qué hace:** Esta clase maneja las solicitudes HTTP. Es el punto de entrada para las operaciones de transferencia y consulta de cuentas.
- **Endpoints:**
    - `POST /transfer`: Recibe una solicitud para transferir dinero entre cuentas.
    - `GET /accounts`: Devuelve todas las cuentas o filtra por nombre.
- **Cómo se integra:** El controlador recibe las solicitudes HTTP, llama a los métodos de `TransferService` y devuelve las respuestas adecuadas.

---

### 6. **Clase `TransferRequest` (DTO - Data Transfer Object)**

```java
package com.example.dto;

import java.math.BigDecimal;

public class TransferRequest {

    private long senderAccountId;
    private long receiverAccountId;
    private BigDecimal amount;

    // Getters y setters
}
```

- **Qué hace:** Esta clase es un DTO (objeto de transferencia de datos) que se usa para recibir datos en el endpoint `/transfer`.
- **Cómo se integra:** Cuando se hace una solicitud POST a `/transfer`, Spring convierte automáticamente el JSON del cuerpo de la solicitud en un objeto `TransferRequest`.

---

### 7. **Clase `AccountNotFoundException` (Excepción personalizada)**

```java
package com.example.exceptions;

public class AccountNotFoundException extends RuntimeException {
}
```

- **Qué hace:** Esta excepción se lanza cuando no se encuentra una cuenta en la base de datos.
- **Cómo se integra:** Se usa en `TransferService` para manejar casos en los que una cuenta no existe.

---

### Flujo de la aplicación

1. **Inicio de la aplicación:**
    - La clase `Main` inicia la aplicación Spring Boot.
    - Spring configura automáticamente los componentes (`@RestController`, `@Service`, `@Repository`).

2. **Solicitud HTTP:**
    - Un cliente (por ejemplo, Postman) hace una solicitud HTTP a uno de los endpoints (`/transfer` o `/accounts`).

3. **Controlador:**
    - El controlador (`AccountController`) recibe la solicitud y llama al servicio correspondiente (`TransferService`).

4. **Servicio:**
    - El servicio (`TransferService`) ejecuta la lógica de negocio y usa el repositorio (`AccountRepository`) para interactuar con la base de datos.

5. **Repositorio:**
    - El repositorio ejecuta las consultas SQL y devuelve los resultados al servicio.

6. **Respuesta:**
    - El servicio devuelve los datos al controlador, que los envía como respuesta HTTP.

---

### Ejecución de la aplicación en Postman

#### 1. **Consultar todas las cuentas:**
- **Método:** GET
- **URL:** `http://localhost:8080/accounts`
- **Respuesta:** Devuelve una lista de todas las cuentas.

#### 2. **Consultar cuentas por nombre:**
- **Método:** GET
- **URL:** `http://localhost:8080/accounts?name=Juan`
- **Respuesta:** Devuelve las cuentas cuyo nombre sea "Juan".

#### 3. **Transferir dinero:**
- **Método:** POST
- **URL:** `http://localhost:8080/transfer`
- **Cuerpo (JSON):**
  ```json
  {
    "senderAccountId": 1,
    "receiverAccountId": 2,
    "amount": 100
  }
  ```
- **Respuesta:** No devuelve contenido (código 200 si la operación es exitosa).

---

### Conclusión

