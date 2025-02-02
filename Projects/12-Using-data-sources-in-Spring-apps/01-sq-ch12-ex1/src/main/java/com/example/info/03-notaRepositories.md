Vamos a desglosar paso a paso el funcionamiento del programa utilizando un **enfoque de primeros principios**. Este enfoque nos permitirá entender cada componente y su propósito desde lo más básico.

---

### **Objetivo del programa**
El programa es un **repositorio** (una clase que interactúa con la base de datos) para la entidad `Purchase`. Su función principal es:
1. **Guardar compras** en la base de datos.
2. **Recuperar todas las compras** almacenadas en la base de datos.

---

### **Componentes principales**
1. **`Purchase`**: Es un modelo que representa una compra con campos como `id`, `product` y `price`.
2. **`JdbcTemplate`**: Es una herramienta de Spring que simplifica el acceso a la base de datos mediante JDBC.
3. **`RowMapper`**: Es una interfaz que mapea filas de una tabla de la base de datos a objetos Java.

---

### **Paso a paso: ¿Qué hace y cómo funciona el programa?**

#### **1. Clase `PurchaseRepository`**
```java
@Repository
public class PurchaseRepository {
  private final JdbcTemplate jdbc;

  public PurchaseRepository(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }
}
```
- **Propósito**: Esta clase es un repositorio que interactúa con la base de datos.
- **Anotación `@Repository`**:
    - Indica que esta clase es un componente de Spring y que su función es acceder a la base de datos.
    - Spring la gestiona como un bean y puede inyectarse en otras clases.
- **Inyección de `JdbcTemplate`**:
    - El constructor recibe una instancia de `JdbcTemplate`, que se inyecta automáticamente por Spring.
    - `JdbcTemplate` es la herramienta que se utiliza para ejecutar consultas SQL y mapear resultados.

---

#### **2. Método `storePurchase`**
```java
public void storePurchase(Purchase purchase) {
  String sql = "INSERT INTO purchase VALUES (NULL, ?, ?)";
  jdbc.update(sql, purchase.getProduct(), purchase.getPrice());
}
```
- **Propósito**: Guarda una compra en la base de datos.
- **Paso a paso**:
    1. **Consulta SQL**:
        - `INSERT INTO purchase VALUES (NULL, ?, ?)` es una consulta SQL que inserta una nueva fila en la tabla `purchase`.
        - `NULL` se usa para el campo `id`, ya que es probable que sea autoincremental.
        - `?` son marcadores de posición para los valores que se insertarán.
    2. **Ejecución de la consulta**:
        - `jdbc.update(sql, purchase.getProduct(), purchase.getPrice())` ejecuta la consulta SQL.
        - Los valores `purchase.getProduct()` y `purchase.getPrice()` se insertan en los marcadores de posición `?`.
    3. **Resultado**:
        - La compra se guarda en la base de datos.

---

#### **3. Método `findAllPurchases`**
```java
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
```
- **Propósito**: Recupera todas las compras almacenadas en la base de datos.
- **Paso a paso**:
    1. **Consulta SQL**:
        - `SELECT * FROM purchase` es una consulta SQL que recupera todas las filas de la tabla `purchase`.
    2. **Mapeo de filas a objetos**:
        - `RowMapper<Purchase>` es una interfaz que convierte cada fila de la tabla en un objeto `Purchase`.
        - La implementación de `RowMapper`:
          ```java
          (r, i) -> {
            Purchase rowObject = new Purchase();
            rowObject.setId(r.getInt("id"));
            rowObject.setProduct(r.getString("product"));
            rowObject.setPrice(r.getBigDecimal("price"));
            return rowObject;
          }
          ```
            - `r` es un `ResultSet` que representa una fila de la tabla.
            - `i` es el índice de la fila.
            - Se crea un objeto `Purchase` y se asignan los valores de las columnas (`id`, `product`, `price`) a los campos del objeto.
    3. **Ejecución de la consulta**:
        - `jdbc.query(sql, purchaseRowMapper)` ejecuta la consulta SQL y utiliza el `RowMapper` para convertir cada fila en un objeto `Purchase`.
    4. **Resultado**:
        - Devuelve una lista de objetos `Purchase` que representan todas las compras en la base de datos.

---

### **Flujo completo del programa**

1. **Guardar una compra**:
    - Se llama al método `storePurchase` con un objeto `Purchase`.
    - Se ejecuta una consulta SQL `INSERT` para guardar la compra en la base de datos.

2. **Recuperar todas las compras**:
    - Se llama al método `findAllPurchases`.
    - Se ejecuta una consulta SQL `SELECT` para recuperar todas las filas de la tabla `purchase`.
    - Cada fila se convierte en un objeto `Purchase` utilizando un `RowMapper`.
    - Se devuelve una lista de objetos `Purchase`.

---

### **Ejemplo de uso**

#### **Guardar una compra**:
```java
Purchase purchase = new Purchase();
purchase.setProduct("Laptop");
purchase.setPrice(new BigDecimal("999.99"));

PurchaseRepository repository = new PurchaseRepository(jdbcTemplate);
repository.storePurchase(purchase);
```
- Esto inserta una nueva compra en la base de datos.

#### **Recuperar todas las compras**:
```java
List<Purchase> purchases = repository.findAllPurchases();
for (Purchase p : purchases) {
    System.out.println(p.getProduct() + " - " + p.getPrice());
}
```
- Esto imprime todos los productos y precios de las compras almacenadas en la base de datos.

---

### **Resumen**
- **`PurchaseRepository`**: Es un repositorio que interactúa con la base de datos.
- **`storePurchase`**: Guarda una compra en la base de datos utilizando `JdbcTemplate`.
- **`findAllPurchases`**: Recupera todas las compras de la base de datos y las convierte en objetos `Purchase` utilizando un `RowMapper`.
- **`JdbcTemplate`**: Simplifica el acceso a la base de datos, manejando conexiones y consultas SQL.
- **`RowMapper`**: Convierte filas de la base de datos en objetos Java.

Este programa es un ejemplo claro de cómo Spring Boot facilita la interacción con bases de datos utilizando herramientas como `JdbcTemplate` y `RowMapper`.