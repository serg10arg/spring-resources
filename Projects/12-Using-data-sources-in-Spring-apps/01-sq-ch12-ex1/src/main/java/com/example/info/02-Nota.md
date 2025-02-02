Vamos a explicar en detalle qué hacen y qué función cumplen los métodos `equals` y `hashCode` en la clase `Purchase`. Estos métodos son fundamentales en Java para comparar objetos y para trabajar con colecciones como `HashSet`, `HashMap`, entre otras.

---

### **Método `equals`**

#### **¿Qué hace?**
El método `equals` se utiliza para comparar si dos objetos son **iguales** en términos de su contenido o estado.

#### **¿Cómo funciona en este caso?**
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true; // 1. Compara si es el mismo objeto en memoria
    if (o == null || getClass() != o.getClass()) return false; // 2. Verifica si el objeto es nulo o de otra clase
    Purchase purchase = (Purchase) o; // 3. Convierte el objeto a tipo Purchase
    return id == purchase.id && // 4. Compara los campos id, product y price
           Objects.equals(product, purchase.product) &&
           Objects.equals(price, purchase.price);
}
```

#### **Explicación paso a paso**:
1. **`if (this == o) return true;`**:
    - Si el objeto que se está comparando (`o`) es el mismo que `this` (es decir, es la misma instancia en memoria), devuelve `true`. Esto es una optimización para evitar comparaciones innecesarias.

2. **`if (o == null || getClass() != o.getClass()) return false;`**:
    - Si el objeto `o` es `null` o no es una instancia de la clase `Purchase`, devuelve `false`. Esto asegura que solo se comparen objetos del mismo tipo.

3. **`Purchase purchase = (Purchase) o;`**:
    - Convierte el objeto `o` a tipo `Purchase` para poder acceder a sus campos.

4. **`return id == purchase.id && Objects.equals(product, purchase.product) && Objects.equals(price, purchase.price);`**:
    - Compara los campos `id`, `product` y `price` de ambos objetos (`this` y `purchase`).
    - Si todos los campos son iguales, devuelve `true`; de lo contrario, devuelve `false`.

---

### **Método `hashCode`**

#### **¿Qué hace?**
El método `hashCode` devuelve un valor entero que representa el objeto. Este valor se utiliza en estructuras de datos como `HashSet` o `HashMap` para almacenar y recuperar objetos de manera eficiente.

#### **¿Cómo funciona en este caso?**
```java
@Override
public int hashCode() {
    return Objects.hash(id, product, price); // Genera un código hash basado en los campos id, product y price
}
```

#### **Explicación paso a paso**:
1. **`Objects.hash(id, product, price);`**:
    - El método `Objects.hash` genera un código hash basado en los valores de los campos `id`, `product` y `price`.
    - Este código hash se utiliza para identificar rápidamente el objeto en colecciones basadas en hash.

---

### **Relación entre `equals` y `hashCode`**

En Java, existe un **contrato** entre los métodos `equals` y `hashCode` que debe cumplirse:
1. Si dos objetos son iguales según `equals`, **deben** tener el mismo código hash (`hashCode`).
2. Si dos objetos tienen el mismo código hash, **no necesariamente** son iguales según `equals`.

#### **¿Por qué es importante?**
- Si no se cumple este contrato, las colecciones basadas en hash (como `HashSet` o `HashMap`) no funcionarán correctamente.
- Por ejemplo, si dos objetos son iguales según `equals` pero tienen diferentes códigos hash, podrían almacenarse en diferentes "cubos" (buckets) de un `HashSet`, lo que causaría comportamientos inesperados.

---

### **Ejemplo de uso**

Supongamos que tenemos dos objetos de la clase `Purchase`:

```java
Purchase purchase1 = new Purchase();
purchase1.setId(1);
purchase1.setProduct("Laptop");
purchase1.setPrice(new BigDecimal("999.99"));

Purchase purchase2 = new Purchase();
purchase2.setId(1);
purchase2.setProduct("Laptop");
purchase2.setPrice(new BigDecimal("999.99"));
```

#### **Comparación con `equals`**:
```java
System.out.println(purchase1.equals(purchase2)); // true
```
- Devuelve `true` porque los campos `id`, `product` y `price` son iguales en ambos objetos.

#### **Código hash con `hashCode`**:
```java
System.out.println(purchase1.hashCode()); // Código hash basado en id, product y price
System.out.println(purchase2.hashCode()); // El mismo código hash que purchase1
```
- Ambos objetos tienen el mismo código hash porque sus campos son iguales.

---

### **Corrección en el método `hashCode`**
Nota: En el código que proporcionaste, hay un error tipográfico en el nombre del método `hashCode`. Debe ser `hashCode` (con una "C" mayúscula), no `hasCode`. Aquí está corregido:

```java
@Override
public int hashCode() {
    return Objects.hash(id, product, price);
}
```

---

### **Resumen**
- **`equals`**: Compara si dos objetos de la clase `Purchase` son iguales en términos de sus campos (`id`, `product` y `price`).
- **`hashCode`**: Genera un código hash basado en los campos `id`, `product` y `price`, que se utiliza en colecciones basadas en hash.
- **Relación**: Si dos objetos son iguales según `equals`, deben tener el mismo código hash.

Estos métodos son esenciales para garantizar que los objetos de la clase `Purchase` se comporten correctamente en colecciones y al realizar comparaciones.