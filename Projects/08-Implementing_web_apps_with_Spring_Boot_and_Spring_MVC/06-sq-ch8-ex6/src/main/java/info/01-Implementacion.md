Este programa es un proyecto en Spring Boot diseñado para gestionar productos (con funcionalidad de agregar y listar productos). A continuación, te explico cómo las clases se integran entre sí para lograr esta funcionalidad:

---

### **1. Clase `Main`**
```java
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```
- Es el **punto de entrada** de la aplicación.
- Utiliza la anotación `@SpringBootApplication`, que:
    - Habilita el escaneo de componentes (`@ComponentScan`).
    - Configura automáticamente el contexto de Spring (`@EnableAutoConfiguration`).
    - Activa la inyección de dependencias.
- La aplicación se inicia con el método `SpringApplication.run()`, que arranca el servidor embebido y registra los componentes necesarios.

---

### **2. Clase `Product`**
```java
public class Product {
    private String name;
    private double price;
    
    // Getters y setters
}
```
- **Función principal**: Representa el modelo de datos, es decir, el concepto de un producto.
- Contiene dos atributos (`name` y `price`) y sus correspondientes métodos `get` y `set`.
- Esta clase actúa como un POJO (Plain Old Java Object), lo que significa que es un simple contenedor de datos sin lógica adicional.

---

### **3. Clase `ProductService`**
```java
@Service
public class ProductService {

    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) {
        products.add(p);
    }

    public List<Product> findAll() {
        return products;
    }
}
```
- Marcada con la anotación `@Service`, que la convierte en un **componente gestionado por Spring** y disponible para la inyección de dependencias.
- **Función principal**: Manejar la lógica del negocio relacionada con los productos.
    - Almacena los productos en una lista local (`products`).
    - Proporciona dos métodos:
        1. `addProduct(Product p)`: Añade un producto a la lista.
        2. `findAll()`: Retorna todos los productos almacenados.
- Es una capa de abstracción que separa la lógica del negocio del resto de la aplicación.

---

### **4. Clase `ProductsController`**
```java
@Controller
public class ProductsController {

    private ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String viewProdcuts(Model model) {
        var products = productService.findAll();
        return "products.html";
    }

    @PostMapping("/products")
    public String addProduct(Product p, Model model) {
        productService.addProduct(p);
        var products = productService.findAll();
        model.addAttribute("products", products);
        return "products.html";
    }
}
```
- Anotada con `@Controller`, lo que la convierte en un controlador Spring MVC.
- **Función principal**: Manejar las peticiones HTTP relacionadas con los productos.
    - **Dependencia de `ProductService`**:
        - Recibe una instancia de `ProductService` mediante inyección de dependencias en su constructor.
    - Métodos principales:
        1. **`viewProducts()`**:
            - Maneja peticiones `GET` a `/products`.
            - Recupera la lista de productos desde el `ProductService`.
            - Retorna la plantilla `products.html`.
        2. **`addProduct(Product p, Model model)`**:
            - Maneja peticiones `POST` a `/products`.
            - Añade un nuevo producto recibido como parámetro (`Product p`) al servicio.
            - Actualiza la lista de productos en el modelo y retorna la plantilla `products.html`.
        - **El objeto `Model`**:
            - Es utilizado para pasar datos desde el controlador a la vista.

---

### **5. Archivo `products.html`**
```html
<h1>Products</h1>
<h2>View products</h2>

<table>
    <tr>
        <th>PRODUCT NAME</th>
        <th>PRODUCT PRICE</th>
    </tr>
    <tr th:each="p: ${products}" >
        <td th:text="${p.name}"></td>
        <td th:text="${p.price}"></td>
    </tr>
</table>

<h2>Add a product</h2>
<form action="/products" method="post">
    Name: <input type="text" name="name"><br />
    Price: <input type="number" step="any" name="price"><br />
    <button type="submit">Add product</button>
</form>
```
- Es una plantilla HTML procesada por Thymeleaf, un motor de plantillas compatible con Spring.
- **Función principal**: Mostrar la interfaz de usuario para listar productos y añadir nuevos productos.
    - **Listar productos**:
        - Usa la directiva `th:each` para iterar sobre la lista de productos (`${products}`) y mostrar su nombre y precio en una tabla.
    - **Añadir productos**:
        - Contiene un formulario con campos para nombre (`name`) y precio (`price`).
        - Al enviar el formulario, realiza una petición `POST` a `/products`.

---

### **Cómo se integran entre sí**

1. **Inicio de la aplicación**:
    - La clase `Main` arranca el servidor Spring y habilita las anotaciones de Spring (`@Service`, `@Controller`, etc.).

2. **Controlador y servicio**:
    - El controlador (`ProductsController`) delega la lógica de negocio al servicio (`ProductService`), que se encarga de gestionar los datos de los productos.

3. **Plantilla y controlador**:
    - La vista `products.html` muestra los productos proporcionados por el modelo y permite enviar datos al controlador para agregar nuevos productos.

4. **Comunicación flujo completo**:
    - Usuario accede a `/products` (método `GET`): Se lista la lista de productos actual.
    - Usuario rellena el formulario y lo envía (método `POST`):
        - El controlador llama a `ProductService` para agregar un nuevo producto.
        - La lista actualizada se pasa al modelo.
        - Se retorna la vista `products.html` con los datos actualizados.

---

### **Conclusión**
Este programa sigue el patrón de diseño **MVC (Modelo-Vista-Controlador)** utilizando Spring Boot y Thymeleaf:
- **Modelo**: `Product`, que representa los datos de un producto.
- **Vista**: `products.html`, que presenta los datos al usuario.
- **Controlador**: `ProductsController`, que maneja las interacciones entre la vista y el modelo.

Todo está orquestado por el framework Spring Boot, que facilita la configuración, la inyección de dependencias y la integración con Thymeleaf para renderizar dinámicamente las vistas.