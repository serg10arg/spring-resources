### 1. ¿Qué es un servicio web REST y su propósito?

Un servicio web REST (Representational State Transfer) es una forma de comunicación entre aplicaciones a través de HTTP. Su propósito es permitir que diferentes sistemas intercambien datos de manera sencilla y estandarizada. REST utiliza los métodos HTTP (GET, POST, PUT, DELETE, etc.) para realizar operaciones sobre recursos, que suelen representarse en formatos como JSON o XML.

Por ejemplo, si tienes una aplicación de tienda en línea, un servicio REST podría permitir a un cliente obtener la lista de productos (GET), agregar un nuevo producto (POST), actualizar un producto existente (PUT) o eliminar un producto (DELETE).

---

### 2. Spring MVC y anotaciones para REST

Spring MVC es un módulo de Spring que facilita la creación de aplicaciones web, incluyendo servicios REST. Para implementar endpoints REST, Spring utiliza anotaciones clave:

- **`@RestController`**: Combina `@Controller` y `@ResponseBody`. Indica que la clase manejará solicitudes HTTP y que los valores devueltos por los métodos se escribirán directamente en el cuerpo de la respuesta HTTP.
- **`@ResponseBody`**: Indica que el valor devuelto por un método se serializará (por ejemplo, a JSON) y se enviará como cuerpo de la respuesta HTTP.

Si no usas estas anotaciones, Spring tratará de resolver el valor devuelto como una vista (por ejemplo, una página JSP), lo que no es deseable en un servicio REST.

---

### 3. Devolver el cuerpo de la respuesta y manejo del estado HTTP

En un controlador REST, puedes devolver directamente un objeto o una colección. Spring se encarga de serializarlo a JSON (u otro formato) y enviarlo en el cuerpo de la respuesta HTTP. Por defecto, el estado HTTP es 200 (OK) para respuestas exitosas.

Ejemplo:
```java
@RestController
public class ProductController {

    @GetMapping("/products")
    public List<Product> getProducts() {
        // Lógica para obtener productos
        return productService.getAllProducts();
    }
}
```
Aquí, `getProducts()` devuelve una lista de productos, y Spring automáticamente la convierte a JSON con un estado HTTP 200.

---

### 4. Uso de `ResponseEntity` para control explícito

`ResponseEntity` te permite controlar explícitamente el estado HTTP y los encabezados de la respuesta. Es útil cuando necesitas devolver un estado HTTP específico (como 201 para "Creado") o agregar encabezados personalizados.

Ejemplo:
```java
@PostMapping("/products")
public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product savedProduct = productService.saveProduct(product);
    return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
}
```
Aquí, se devuelve un estado HTTP 201 (CREATED) junto con el producto creado.

---

### 5. Manejo de excepciones

Hay dos enfoques para manejar excepciones en Spring:

1. **Directamente en el controlador**:
   ```java
   @GetMapping("/products/{id}")
   public Product getProductById(@PathVariable Long id) {
       return productService.getProductById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
   }
   ```

2. **Usando `@RestControllerAdvice`**:
   ```java
   @RestControllerAdvice
   public class GlobalExceptionHandler {

       @ExceptionHandler(ResourceNotFoundException.class)
       public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
           return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
       }
   }
   ```
   Este enfoque evita la duplicación de código y centraliza el manejo de excepciones.

---

### 6. Recibir datos del cliente

Un endpoint REST puede recibir datos del cliente de varias formas:

- **Parámetros de solicitud**: Usando `@RequestParam`.
  ```java
  @GetMapping("/products")
  public List<Product> getProductsByCategory(@RequestParam String category) {
      return productService.getProductsByCategory(category);
  }
  ```

- **Variables de ruta**: Usando `@PathVariable`.
  ```java
  @GetMapping("/products/{id}")
  public Product getProductById(@PathVariable Long id) {
      return productService.getProductById(id);
  }
  ```

- **Cuerpo de la solicitud**: Usando `@RequestBody`.
  ```java
  @PostMapping("/products")
  public Product createProduct(@RequestBody Product product) {
      return productService.saveProduct(product);
  }
  ```

---

### 7. Ejemplo práctico

Aquí tienes un ejemplo completo de un controlador REST en Spring:

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
```

---

### 8. Analogía para entender REST

Imagina un servicio REST como un camarero en un restaurante:

- **El cliente (cliente HTTP)** hace un pedido (solicitud HTTP) al camarero (servidor REST).
- El camarero entiende el pedido (método HTTP: GET, POST, etc.) y lo procesa.
- Luego, el camarero devuelve lo que el cliente pidió (respuesta HTTP), ya sea un plato de comida (datos en JSON) o un mensaje de error si algo salió mal.

Esta analogía ayuda a entender cómo los servicios REST actúan como intermediarios entre el cliente y el servidor, facilitando la comunicación de manera eficiente y estandarizada.