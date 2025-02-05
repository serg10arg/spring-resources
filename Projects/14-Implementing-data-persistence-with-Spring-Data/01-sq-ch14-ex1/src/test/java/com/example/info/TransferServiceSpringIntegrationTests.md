Esta clase de pruebas utiliza **Spring Boot Test** y **Mockito** para realizar una prueba de integración del servicio `TransferService`. A diferencia de las pruebas unitarias anteriores, esta prueba está diseñada para ejecutarse en un contexto de Spring, lo que permite probar la integración entre el servicio y sus dependencias (en este caso, `AccountRepository`) dentro del contexto de Spring. Vamos a desglosar y explicar cada parte de esta clase de pruebas desde los principios fundamentales, dirigido a un público principiante.

---

### 1. **Anotaciones Clave**

#### **`@SpringBootTest`**
- Esta anotación le dice a Spring Boot que cargue el contexto completo de la aplicación para la prueba. Esto permite que los beans de Spring (como `TransferService` y `AccountRepository`) estén disponibles en el contexto de la prueba.
- Es útil para pruebas de integración, donde se necesita probar la interacción entre varios componentes del sistema.

#### **`@MockBean`**
- Esta anotación crea un objeto simulado (mock) de la clase `AccountRepository` y lo registra en el contexto de Spring.
- El mock se utiliza para simular el comportamiento de `AccountRepository` sin necesidad de una implementación real.

#### **`@Autowired`**
- Esta anotación inyecta automáticamente el bean de `TransferService` desde el contexto de Spring en la prueba.
- Esto permite probar el servicio en un entorno realista, donde las dependencias están gestionadas por Spring.

---

### 2. **Método de Prueba: `transferServiceTransferAmountTest`**

Este método prueba el flujo feliz (happy path) de la transferencia de dinero, donde todo funciona correctamente.

#### **Configuración de los Mocks**
- Se crean dos cuentas: `sender` (cuenta emisora) y `receiver` (cuenta receptora).
- Se configura el mock de `AccountRepository` para que devuelva estas cuentas cuando se llame a `findById` con los IDs correspondientes:
  ```java
  when(accountRepository.findById(1L)).thenReturn(Optional.of(sender));
  when(accountRepository.findById(2L)).thenReturn(Optional.of(receiver));
  ```

#### **Ejecución del Método bajo Prueba**
- Se llama al método `transferMoney` del `TransferService` para transferir 100 unidades de la cuenta `sender` a la cuenta `receiver`.

#### **Verificación del Comportamiento**
- Se verifica que el método `changeAmount` del `AccountRepository` se haya llamado con los valores correctos:
  ```java
  verify(accountRepository).changeAmount(1, new BigDecimal(900)); // El saldo de sender se reduce en 100.
  verify(accountRepository).changeAmount(2, new BigDecimal(1100)); // El saldo de receiver aumenta en 100.
  ```

---

### 3. **Principios Fundamentales**

#### **Pruebas de Integración**
- A diferencia de las pruebas unitarias, las pruebas de integración verifican la interacción entre varios componentes del sistema. En este caso, se prueba la integración entre `TransferService` y `AccountRepository` dentro del contexto de Spring.

#### **Contexto de Spring**
- El uso de `@SpringBootTest` permite cargar el contexto completo de Spring, lo que facilita la prueba de beans gestionados por Spring y sus dependencias.

#### **Mocking en el Contexto de Spring**
- El uso de `@MockBean` permite crear mocks que se integran con el contexto de Spring. Esto es útil para simular dependencias externas (como `AccountRepository`) sin necesidad de una implementación real.

#### **Verificación del Comportamiento**
- Las pruebas de integración también deben verificar que el comportamiento del sistema es el esperado. Aquí, se verifica que los métodos `changeAmount` se llaman con los valores correctos después de la transferencia.

---

### 4. **Diferencias con las Pruebas Unitarias**

- **Contexto de Spring**: En las pruebas unitarias, el contexto de Spring no se carga, y las dependencias se gestionan manualmente (por ejemplo, usando `@Mock` e `@InjectMocks`). En las pruebas de integración, el contexto de Spring se carga, lo que permite probar la integración entre componentes.

- **Mocking**: En las pruebas unitarias, los mocks se crean manualmente. En las pruebas de integración, los mocks se registran en el contexto de Spring usando `@MockBean`.

- **Alcance**: Las pruebas unitarias se centran en una sola unidad de código (por ejemplo, un método), mientras que las pruebas de integración prueban la interacción entre varios componentes.

---

### 5. **Resumen**

Esta clase de pruebas de integración demuestra cómo probar el método `transferMoney` de `TransferService` en un contexto de Spring. Se cubre el flujo feliz, donde la transferencia se realiza correctamente y se actualizan los saldos de las cuentas.

Para un principiante, es importante entender que:
- Las pruebas de integración son útiles para verificar la interacción entre componentes en un entorno realista.
- El uso de `@SpringBootTest` y `@MockBean` simplifica la creación de pruebas de integración en aplicaciones Spring Boot.
- Las pruebas de integración complementan las pruebas unitarias, ya que cubren aspectos diferentes del comportamiento del sistema.