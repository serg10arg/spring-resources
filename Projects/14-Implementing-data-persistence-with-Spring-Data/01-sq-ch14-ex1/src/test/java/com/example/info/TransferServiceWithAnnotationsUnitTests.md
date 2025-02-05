Esta clase de pruebas unitarias utiliza JUnit 5 y Mockito para probar el comportamiento de la clase `TransferService`, específicamente el método `transferMoney`. A diferencia del ejemplo anterior, este código utiliza anotaciones de Mockito para simplificar la creación de mocks y la inyección de dependencias. Vamos a desglosar y explicar cada parte de esta clase de pruebas desde los principios fundamentales, dirigido a un público principiante.

---

### 1. **Anotaciones Clave**

#### **`@ExtendWith(MockitoExtension.class)`**
- Esta anotación le dice a JUnit que utilice la extensión de Mockito para gestionar automáticamente los mocks y las inyecciones de dependencias.
- Simplifica la configuración de las pruebas, ya que no es necesario inicializar manualmente los mocks o inyectarlos en el objeto bajo prueba.

#### **`@Mock`**
- Esta anotación crea un objeto simulado (mock) de la clase `AccountRepository`.
- El mock se utiliza para simular el comportamiento de `AccountRepository` sin necesidad de una implementación real.

#### **`@InjectMocks`**
- Esta anotación crea una instancia de la clase `TransferService` e inyecta automáticamente los mocks (en este caso, `accountRepository`) en sus dependencias.
- Esto evita la necesidad de crear manualmente el objeto `TransferService` y pasarle el mock.

---

### 2. **Método de Prueba: `moneyTransferHappyFlow`**

Este método prueba el flujo feliz (happy path) de la transferencia de dinero, donde todo funciona correctamente.

#### **Configuración de los Mocks**
- Se crean dos cuentas: `sender` (cuenta emisora) y `destination` (cuenta receptora).
- Se configura el mock de `AccountRepository` para que devuelva estas cuentas cuando se llame a `findById` con los IDs correspondientes:
  ```java
  given(accountRepository.findById(sender.getId())).willReturn(Optional.of(sender));
  given(accountRepository.findById(destination.getId())).willReturn(Optional.of(destination));
  ```

#### **Ejecución del Método bajo Prueba**
- Se llama al método `transferMoney` del `TransferService` para transferir 100 unidades de la cuenta `sender` a la cuenta `destination`.

#### **Verificación del Comportamiento**
- Se verifica que el método `changeAmount` del `AccountRepository` se haya llamado con los valores correctos:
  ```java
  verify(accountRepository).changeAmount(1, new BigDecimal(900)); // El saldo de sender se reduce en 100.
  verify(accountRepository).changeAmount(2, new BigDecimal(1100)); // El saldo de destination aumenta en 100.
  ```

---

### 3. **Método de Prueba: `moneyTransferDestinationAccountNotFoundFlow`**

Este método prueba un flujo alternativo donde la cuenta de destino no existe, lo que debería lanzar una excepción `AccountNotFoundException`.

#### **Configuración de los Mocks**
- Se crea una cuenta `sender` y se configura el mock de `AccountRepository` para devolverla cuando se llame a `findById` con el ID 1.
- Se configura el mock para devolver un `Optional.empty()` cuando se llame a `findById` con el ID 2, simulando que la cuenta de destino no existe:
  ```java
  given(accountRepository.findById(1L)).willReturn(Optional.of(sender));
  given(accountRepository.findById(2L)).willReturn(Optional.empty());
  ```

#### **Ejecución del Método bajo Prueba**
- Se llama al método `transferMoney` y se espera que lance una excepción `AccountNotFoundException`:
  ```java
  assertThrows(
      AccountNotFoundException.class,
      () -> transferService.transferMoney(1, 2, new BigDecimal(100))
  );
  ```

#### **Verificación del Comportamiento**
- Se verifica que el método `changeAmount` **nunca** se haya llamado, ya que la transferencia no debería realizarse si la cuenta de destino no existe:
  ```java
  verify(accountRepository, never()).changeAmount(anyLong(), any());
  ```

---

### 4. **Principios Fundamentales**

#### **Aislamiento**
- Las pruebas unitarias deben aislar la unidad de código que se está probando. En este caso, se utiliza un mock de `AccountRepository` para asegurarse de que la prueba solo se centra en el comportamiento de `TransferService`.

#### **Verificación**
- Las pruebas unitarias deben verificar que el código hace lo que se supone que debe hacer. Aquí, se verifica que los métodos se llaman con los valores correctos o que se lanzan las excepciones esperadas.

#### **Simplicidad**
- El uso de anotaciones como `@Mock` e `@InjectMocks` simplifica la configuración de las pruebas, haciéndolas más fáciles de escribir y mantener.

#### **Cobertura de Casos**
- Se prueban tanto el flujo feliz (`moneyTransferHappyFlow`) como un flujo alternativo (`moneyTransferDestinationAccountNotFoundFlow`), lo que asegura que el código maneje correctamente diferentes escenarios.

---

### 5. **Resumen**

Esta clase de pruebas unitarias demuestra cómo probar el método `transferMoney` de `TransferService` utilizando JUnit 5 y Mockito. Se cubren dos escenarios principales:
1. **Flujo feliz**: La transferencia se realiza correctamente y se actualizan los saldos de las cuentas.
2. **Flujo alternativo**: La cuenta de destino no existe, por lo que se lanza una excepción y no se realizan cambios en los saldos.

