Vamos a desglosar paso a paso cómo funciona este programa utilizando un **enfoque de primeros principios**, enfocado en un público principiante en Spring Boot y el manejo de bases de datos. Además, te explicaré cómo ejecutar y probar el programa utilizando **Postman**.

---

### **Objetivo del programa**
Este programa es una aplicación Spring Boot que:
1. Se conecta a una base de datos MySQL.
2. Expone dos endpoints REST:
    - **POST `/purchase`**: Para guardar una compra en la base de datos.
    - **GET `/purchase`**: Para recuperar todas las compras almacenadas en la base de datos.

---

### **Componentes principales**

1. **`Purchase`**: Es un modelo que representa una compra con campos como `id`, `product` y `price`.
2. **`PurchaseRepository`**: Es un repositorio que interactúa con la base de datos para guardar y recuperar compras.
3. **`PurchaseController`**: Es un controlador REST que expone los endpoints para guardar y recuperar compras.
4. **Base de datos MySQL**: Almacena las compras en la tabla `purchase`.

---

### **Paso a paso: ¿Qué hace y cómo funciona el programa?**

#### **1. Configuración de la base de datos**
El archivo `application.properties` define cómo la aplicación se conecta a la base de datos MySQL.

##### **Archivo `application.properties`**:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_quickly
spring.datasource.username=root
spring.datasource.password=[your_password]
spring.sql.init.mode=always
```

- **`spring.datasource.url`**: Especifica la URL de conexión a la base de datos MySQL.
    - `jdbc:mysql://localhost:3306/spring_quickly`: Indica que la base de datos `spring_quickly` está en `localhost` en el puerto `3306`.
- **`spring.datasource.username`**: Nombre de usuario para conectarse a MySQL (en este caso, `root`).
- **`spring.datasource.password`**: Contraseña del usuario.
- **`spring.sql.init.mode=always`**: Indica que Spring Boot ejecutará scripts de inicialización de la base de datos (como `schema.sql`) cada vez que la aplicación se inicie.

---

#### **2. Script SQL (`schema.sql`)**
Este script se ejecuta automáticamente al iniciar la aplicación para crear la tabla `purchase` en la base de datos.

##### **Script SQL**:
```sql
DROP TABLE IF EXISTS purchase CASCADE;

CREATE TABLE IF NOT EXISTS purchase (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product varchar(50) NOT NULL,
    price double NOT NULL
);
```

- **`DROP TABLE IF EXISTS purchase CASCADE;`**: Elimina la tabla `purchase` si ya existe.
- **`CREATE TABLE IF NOT EXISTS purchase (...)`**: Crea la tabla `purchase` con las columnas `id`, `product` y `price`.

---

#### **3. Clase `Purchase`**
Es un modelo que representa una compra.

##### **Código**:
```java
package com.example.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Purchase {
    private int id;
    private String product;
    private BigDecimal price;

    // Getters y setters
    @Override
    public boolean equals(Object o) { ... }
    @Override
    public int hashCode() { ... }
}
```

- **Campos**:
    - `id`: Identificador único de la compra.
    - `product`: Nombre del producto.
    - `price`: Precio del producto.
- **Métodos `equals` y `hashCode`**: Se utilizan para comparar objetos `Purchase`.

---

#### **4. Clase `PurchaseRepository`**
Es un repositorio que interactúa con la base de datos.

##### **Código**:
```java
package com.example.repositories;

import com.example.model.Purchase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PurchaseRepository {
    private final JdbcTemplate jdbc;

    public PurchaseRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void storePurchase(Purchase purchase) {
        String sql = "INSERT INTO purchase VALUES (NULL, ?, ?)";
        jdbc.update(sql, purchase.getProduct(), purchase.getPrice());
    }

    public List<Purchase> findAllPurchases() {
        String sql = "SELECT * FROM purchase";

        RowMapper<Purchase> purchaseRowMapper = (r, i) -> {
            Purchase rowObject = new Purchase();
            rowObject.setId(r.getInt("id"));
            rowObject.setProduct(r.getString("product"));
            rowObject.setPrice(r.getBigDecimal("price"));
            return rowObject;
        };

        return jdbc.query(sql, purchaseRowMapper);
    }
}
```

- **`storePurchase`**: Guarda una compra en la base de datos.
- **`findAllPurchases`**: Recupera todas las compras de la base de datos.

---

#### **5. Clase `PurchaseController`**
Es un controlador REST que expone los endpoints.

##### **Código**:
```java
package com.example.controllers;

import com.example.model.Purchase;
import com.example.repositories.PurchaseRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    private final PurchaseRepository purchaseRepository;

    public PurchaseController(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @PostMapping
    public void storePurchase(@RequestBody Purchase purchase) {
        purchaseRepository.storePurchase(purchase);
    }

    @GetMapping
    public List<Purchase> findPurchases() {
        return purchaseRepository.findAllPurchases();
    }
}
```

- **`POST /purchase`**: Recibe un objeto `Purchase` en el cuerpo de la solicitud y lo guarda en la base de datos.
- **`GET /purchase`**: Devuelve una lista de todas las compras almacenadas en la base de datos.

---

#### **6. Clase `Main`**
Es el punto de entrada de la aplicación Spring Boot.

##### **Código**:
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

- **`@SpringBootApplication`**: Habilita la configuración automática y el escaneo de componentes.
- **`SpringApplication.run(Main.class, args)`**: Inicia la aplicación Spring Boot.

---

### **Cómo ejecutar y probar el programa en Postman**

#### **1. Ejecutar la aplicación**
1. Asegúrate de tener **MySQL** instalado y en ejecución.
2. Crea la base de datos `spring_quickly` en MySQL:
   ```sql
   CREATE DATABASE spring_quickly;
   ```
3. Ejecuta la aplicación Spring Boot:
    - Si usas Maven:
      ```bash
      mvn spring-boot:run
      ```
    - Si usas Gradle:
      ```bash
      ./gradlew bootRun
      ```

---

#### **2. Probar en Postman**

##### **Guardar una compra (POST)**:
1. Abre Postman.
2. Configura la solicitud:
    - **Método**: POST
    - **URL**: `http://localhost:8080/purchase`
    - **Headers**: Asegúrate de que `Content-Type` sea `application/json`.
    - **Body** (raw, JSON):
      ```json
      {
        "product": "Laptop",
        "price": 999.99
      }
      ```
3. Haz clic en **Send**.
4. Verifica que la respuesta tenga un código de estado `200 OK`.

##### **Recuperar todas las compras (GET)**:
1. Configura la solicitud:
    - **Método**: GET
    - **URL**: `http://localhost:8080/purchase`
2. Haz clic en **Send**.
3. Verifica que la respuesta sea una lista de compras en formato JSON:
   ```json
   [
     {
       "id": 1,
       "product": "Laptop",
       "price": 999.99
     }
   ]
   ```

---

### **Resumen**
- **`Purchase`**: Representa una compra.
- **`PurchaseRepository`**: Interactúa con la base de datos para guardar y recuperar compras.
- **`PurchaseController`**: Expone endpoints REST para guardar y recuperar compras.
- **Base de datos MySQL**: Almacena las compras en la tabla `purchase`.
- **Postman**: Se utiliza para probar los endpoints REST.

--