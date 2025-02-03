
---

### **1. `return jdbc.queryForObject(sql, new AccountRowMapper(), id);`**

#### **¿Qué hace?**
Este método se utiliza para **obtener un único objeto** de la base de datos. En este caso, se usa para buscar una cuenta por su ID.

#### **Desglose de la sintaxis**:
- **`jdbc`**: Es una instancia de `JdbcTemplate`, que es una herramienta de Spring para interactuar con la base de datos.
- **`queryForObject`**: Es un método de `JdbcTemplate` que ejecuta una consulta SQL y espera un único resultado.
- **`sql`**: Es la consulta SQL que se va a ejecutar. En este caso, es `"SELECT * FROM account WHERE id = ?"`.
- **`new AccountRowMapper()`**: Es una instancia de `AccountRowMapper`, que se encarga de convertir una fila de la base de datos en un objeto `Account`.
- **`id`**: Es el valor que se pasa como parámetro para reemplazar el `?` en la consulta SQL.

#### **¿Cómo funciona?**
1. La consulta SQL se ejecuta en la base de datos.
2. Si se encuentra una fila que coincide con el ID proporcionado, `AccountRowMapper` convierte esa fila en un objeto `Account`.
3. El objeto `Account` se devuelve como resultado.

#### **Relación con otras clases**:
- **`Account`**: El resultado de esta consulta es un objeto `Account`, que representa una cuenta bancaria.
- **`AccountRowMapper`**: Es el "traductor" que convierte los datos de la base de datos en un objeto `Account`.
- **`TransferService`**: Este servicio llama a `findAccountById` para obtener las cuentas involucradas en una transferencia.

#### **Ejemplo**:
Si llamas a `findAccountById(1)`, la consulta SQL será:
```sql
SELECT * FROM account WHERE id = 1;
```
Si la base de datos tiene una cuenta con ID 1, `AccountRowMapper` convertirá esa fila en un objeto `Account` y lo devolverá.

---

### **2. `return jdbc.query(sql, new AccountRowMapper());`**

#### **¿Qué hace?**
Este método se utiliza para **obtener una lista de objetos** de la base de datos. En este caso, se usa para obtener todas las cuentas.

#### **Desglose de la sintaxis**:
- **`jdbc`**: Es la instancia de `JdbcTemplate`.
- **`query`**: Es un método de `JdbcTemplate` que ejecuta una consulta SQL y devuelve una lista de resultados.
- **`sql`**: Es la consulta SQL que se va a ejecutar. En este caso, es `"SELECT * FROM account"`.
- **`new AccountRowMapper()`**: Es una instancia de `AccountRowMapper`, que convierte cada fila de la base de datos en un objeto `Account`.

#### **¿Cómo funciona?**
1. La consulta SQL se ejecuta en la base de datos.
2. Para cada fila en el resultado, `AccountRowMapper` convierte la fila en un objeto `Account`.
3. Se devuelve una lista de objetos `Account`.

#### **Relación con otras clases**:
- **`Account`**: Cada elemento de la lista es un objeto `Account`.
- **`AccountRowMapper`**: Convierte cada fila de la base de datos en un objeto `Account`.
- **`AccountController`**: Este controlador llama a `findAllAccounts` para obtener todas las cuentas y devolverlas como respuesta a una solicitud HTTP.

#### **Ejemplo**:
Si la base de datos tiene dos cuentas, la consulta SQL será:
```sql
SELECT * FROM account;
```
El resultado será una lista con dos objetos `Account`, cada uno representando una cuenta.

---

### **3. `jdbc.update(sql, amount, id);`**

#### **¿Qué hace?**
Este método se utiliza para **actualizar datos** en la base de datos. En este caso, se usa para cambiar el saldo de una cuenta.

#### **Desglose de la sintaxis**:
- **`jdbc`**: Es la instancia de `JdbcTemplate`.
- **`update`**: Es un método de `JdbcTemplate` que ejecuta una consulta SQL de actualización (INSERT, UPDATE, DELETE).
- **`sql`**: Es la consulta SQL que se va a ejecutar. En este caso, es `"UPDATE account SET amount = ? WHERE id = ?"`.
- **`amount`**: Es el nuevo saldo que se asignará a la cuenta.
- **`id`**: Es el ID de la cuenta que se va a actualizar.

#### **¿Cómo funciona?**
1. La consulta SQL se ejecuta en la base de datos.
2. Los valores `amount` e `id` se pasan como parámetros para reemplazar los `?` en la consulta SQL.
3. La base de datos actualiza el saldo de la cuenta con el ID proporcionado.

#### **Relación con otras clases**:
- **`Account`**: El saldo de la cuenta se actualiza en la base de datos.
- **`TransferService`**: Este servicio llama a `changeAmount` para actualizar los saldos de las cuentas involucradas en una transferencia.
- **Transacciones**: Si este método se llama dentro de una transacción y algo sale mal, Spring revertirá los cambios.

#### **Ejemplo**:
Si llamas a `changeAmount(1, 500)`, la consulta SQL será:
```sql
UPDATE account SET amount = 500 WHERE id = 1;
```
Esto actualizará el saldo de la cuenta con ID 1 a 500.

---

### **4. Resumen de las relaciones**

1. **`AccountRepository`**:
    - Usa `JdbcTemplate` para ejecutar consultas SQL.
    - Usa `AccountRowMapper` para convertir filas de la base de datos en objetos `Account`.

2. **`Account`**:
    - Es el modelo de datos que representa una cuenta bancaria.
    - Los métodos de `AccountRepository` devuelven o actualizan objetos `Account`.

3. **`TransferService`**:
    - Llama a los métodos de `AccountRepository` para obtener y actualizar cuentas.
    - Maneja la lógica de negocio, como las transferencias de dinero.

4. **`AccountController`**:
    - Expone los métodos de `TransferService` como endpoints REST.
    - Recibe solicitudes HTTP y devuelve respuestas.

5. **Transacciones**:
    - Si `changeAmount` se llama dentro de una transacción y algo sale mal, Spring revierte los cambios.

---

### **5. Ejemplo de flujo completo**

1. **Transferencia de dinero**:
    - El controlador recibe una solicitud para transferir dinero.
    - El servicio llama a `findAccountById` para obtener las cuentas del remitente y del receptor.
    - El servicio llama a `changeAmount` para actualizar los saldos de las cuentas.
    - Si todo va bien, los cambios se guardan en la base de datos. Si algo falla, Spring revierte los cambios.

2. **Obtener todas las cuentas**:
    - El controlador recibe una solicitud para obtener todas las cuentas.
    - El servicio llama a `findAllAccounts` para obtener la lista de cuentas.
    - El controlador devuelve la lista como respuesta.

---

