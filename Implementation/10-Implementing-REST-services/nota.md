Un **endpoint REST** es una URL específica dentro de una API basada en REST (Representational State Transfer) que permite a los clientes interactuar con el servidor mediante peticiones HTTP. Básicamente, es el punto de acceso a un recurso en un sistema web.

### **Elementos clave de un endpoint REST**
1. **URL Base**: Define la ubicación del servidor.
    - Ejemplo: `https://api.ejemplo.com`

2. **Recurso**: Representa un objeto o entidad en la API.
    - Ejemplo: `/usuarios`

3. **Método HTTP**: Indica la acción a realizar sobre el recurso.
    - `GET` → Obtener datos
    - `POST` → Crear un nuevo recurso
    - `PUT` → Actualizar un recurso existente
    - `DELETE` → Eliminar un recurso

4. **Parámetros y Query Strings**: Permiten modificar la solicitud.
    - Parámetros de ruta: `/usuarios/{id}` → `https://api.ejemplo.com/usuarios/5`
    - Query String: `?orden=asc&limite=10` → `https://api.ejemplo.com/usuarios?orden=asc&limite=10`

### **Ejemplo de endpoints REST**
| Método | Endpoint | Acción |
|--------|---------|--------|
| `GET` | `/usuarios` | Obtener lista de usuarios |
| `GET` | `/usuarios/{id}` | Obtener un usuario específico |
| `POST` | `/usuarios` | Crear un usuario |
| `PUT` | `/usuarios/{id}` | Actualizar un usuario |
| `DELETE` | `/usuarios/{id}` | Eliminar un usuario |

### **Ejemplo en Spring Boot**
```java
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping("/{id}")
    public Usuario obtenerUsuario(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }
}
```

En este código:
- `@GetMapping("/{id}")` define un endpoint REST que responde a `GET /usuarios/{id}`
- `@PostMapping` define un endpoint que responde a `POST /usuarios`

### **Conclusión**
Un **endpoint REST** es la combinación de una URL, un recurso y un método HTTP que permite la comunicación entre clientes y servidores en una API RESTful.