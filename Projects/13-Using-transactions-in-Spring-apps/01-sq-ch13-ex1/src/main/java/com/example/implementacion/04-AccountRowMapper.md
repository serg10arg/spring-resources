
---

### **1. ¿Qué es un `RowMapper`?**

En Spring, un **`RowMapper`** es una interfaz que se utiliza para convertir filas de una tabla de base de datos en objetos Java. Piensa en él como un "traductor" que toma los datos de la base de datos (que están en forma de filas) y los convierte en objetos que tu aplicación puede entender y usar.

---

### **2. ¿Qué hace esta clase `AccountRowMapper`?**

La clase `AccountRowMapper` implementa la interfaz `RowMapper<Account>`, lo que significa que su trabajo es convertir filas de la tabla `account` en objetos `Account`.

#### **Desglose de la clase**:

1. **`implements RowMapper<Account>`**:
    - Esto indica que la clase es un `RowMapper` que trabaja con objetos de tipo `Account`.

2. **`mapRow(ResultSet resultSet, int i)`**:
    - Este es el método principal que debes implementar cuando usas `RowMapper`.
    - Recibe un `ResultSet` (que representa una fila de la base de datos) y un índice `i` (que indica el número de la fila).
    - Devuelve un objeto `Account` creado a partir de los datos de la fila.

---

### **3. ¿Cómo funciona el método `mapRow`?**

El método `mapRow` toma una fila de la base de datos y la convierte en un objeto `Account`. Aquí está el desglose paso a paso:

1. **Crear un objeto `Account`**:
    - Se crea una nueva instancia de `Account` usando `Account a = new Account();`.

2. **Asignar valores desde el `ResultSet`**:
    - **`a.setId(resultSet.getInt("id"));`**:
        - Obtiene el valor de la columna `id` de la fila actual y lo asigna al atributo `id` del objeto `Account`.
    - **`a.setName(resultSet.getString("name"));`**:
        - Obtiene el valor de la columna `name` de la fila actual y lo asigna al atributo `name` del objeto `Account`.
    - **`a.setAmount(resultSet.getBigDecimal("amount"));`**:
        - Obtiene el valor de la columna `amount` de la fila actual y lo asigna al atributo `amount` del objeto `Account`.

3. **Devolver el objeto `Account`**:
    - Una vez que se han asignado todos los valores, el método devuelve el objeto `Account` creado.

---

### **4. ¿Por qué es necesario un `RowMapper`?**

Cuando ejecutas una consulta SQL en una base de datos, los resultados se devuelven en forma de filas. Sin embargo, en tu aplicación, necesitas trabajar con objetos Java (como `Account`). Aquí es donde entra `RowMapper`:

- **Problema**: La base de datos devuelve datos en forma de tablas, pero tu aplicación necesita objetos.
- **Solución**: `RowMapper` convierte las filas de la base de datos en objetos Java.

---

### **5. Relación con otras clases**

#### **a) `Account`**:
- Es el modelo de datos que representa una cuenta bancaria.
- `AccountRowMapper` convierte filas de la base de datos en objetos `Account`.

#### **b) `AccountRepository`**:
- Es la clase que interactúa con la base de datos.
- Usa `AccountRowMapper` para convertir los resultados de las consultas SQL en objetos `Account`.

#### **c) `TransferService`**:
- Es la clase que contiene la lógica de negocio.
- Usa `AccountRepository` para obtener y actualizar cuentas.

---

### **6. Ejemplo de uso**

Imagina que tienes la siguiente tabla en la base de datos:

| id  | name       | amount |
|-----|------------|--------|
| 1   | Helen Down | 1000   |
| 2   | Peter Read | 1500   |

Cuando ejecutas una consulta SQL como `SELECT * FROM account`, la base de datos devuelve estas filas. `AccountRowMapper` convierte cada fila en un objeto `Account`:

- Para la fila 1:
    - `id = 1`
    - `name = "Helen Down"`
    - `amount = 1000`

- Para la fila 2:
    - `id = 2`
    - `name = "Peter Read"`
    - `amount = 1500`

---

### **7. Resumen**

- **`AccountRowMapper`** es una clase que implementa `RowMapper<Account>`.
- Su trabajo es convertir filas de la base de datos en objetos `Account`.
- Usa el método `mapRow` para asignar los valores de las columnas a los atributos del objeto `Account`.
- Es utilizado por `AccountRepository` para convertir los resultados de las consultas SQL en objetos Java.

---

### **8. ¿Cómo encaja en el flujo de la aplicación?**

1. **Consulta a la base de datos**:
    - `AccountRepository` ejecuta una consulta SQL (por ejemplo, `SELECT * FROM account`).

2. **Mapeo de filas a objetos**:
    - `AccountRowMapper` convierte cada fila del resultado en un objeto `Account`.

3. **Uso en la aplicación**:
    - Los objetos `Account` se devuelven a `TransferService` y luego a `AccountController`.
    - Finalmente, se envían como respuesta en formato JSON.

---

### **9. Ejemplo de código en `AccountRepository`**

Aquí hay un ejemplo de cómo se usa `AccountRowMapper` en `AccountRepository`:

```java
public Account findAccountById(long id) {
    String sql = "SELECT * FROM account WHERE id = ?";
    return jdbc.queryForObject(sql, new AccountRowMapper(), id);
}
```

- **`queryForObject`**: Ejecuta la consulta SQL y espera un único resultado.
- **`new AccountRowMapper()`**: Crea una instancia de `AccountRowMapper` para convertir la fila en un objeto `Account`.
- **`id`**: Es el valor que se pasa como parámetro para reemplazar el `?` en la consulta SQL.

---

