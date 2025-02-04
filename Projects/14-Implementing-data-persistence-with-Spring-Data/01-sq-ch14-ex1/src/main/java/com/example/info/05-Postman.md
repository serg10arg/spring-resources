Para comprobar esta aplicación en Postman, debes realizar pruebas a los endpoints expuestos por los controladores. En este caso, el controlador `AccountController` tiene dos endpoints principales:

1. **`POST /transfer`**: Para realizar una transferencia de dinero entre cuentas.
2. **`GET /accounts`**: Para obtener todas las cuentas o filtrarlas por nombre.

A continuación, te explico cómo probar cada uno de estos endpoints en Postman.

---

### **1. Configuración Inicial**

Antes de comenzar, asegúrate de que:

- La aplicación Spring Boot esté en ejecución.
- Postman esté instalado en tu computadora.
- Tienes una base de datos configurada con al menos dos cuentas para probar las transferencias.

---

### **2. Probar el Endpoint `POST /transfer`**

Este endpoint permite realizar una transferencia de dinero entre dos cuentas. Necesitas enviar un JSON en el cuerpo de la solicitud con los IDs de las cuentas y el monto a transferir.

#### **Paso a Paso:**

1. **Abrir Postman**.
2. **Crear una nueva solicitud**:
    - Método: **POST**.
    - URL: `http://localhost:8080/transfer` (asumiendo que la aplicación corre en el puerto 8080).
3. **Configurar el cuerpo de la solicitud**:
    - Selecciona la pestaña **Body**.
    - Elige **raw** y selecciona **JSON** en el menú desplegable.
    - Escribe el siguiente JSON en el cuerpo:

      ```json
      {
        "senderAccountId": 1,
        "receiverAccountId": 2,
        "amount": 100.00
      }
      ```

        - **`senderAccountId`**: ID de la cuenta que envía el dinero.
        - **`receiverAccountId`**: ID de la cuenta que recibe el dinero.
        - **`amount`**: Cantidad a transferir.

4. **Enviar la solicitud**:
    - Haz clic en **Send**.
    - Si todo está correcto, recibirás una respuesta con código **200 OK** (sin cuerpo, ya que el método no devuelve nada).

5. **Verificar en la base de datos**:
    - Revisa las cuentas con IDs 1 y 2 en la base de datos para confirmar que los saldos se actualizaron correctamente.

---

### **3. Probar el Endpoint `GET /accounts`**

Este endpoint permite obtener todas las cuentas o filtrarlas por nombre.

#### **Caso 1: Obtener todas las cuentas**

1. **Abrir Postman**.
2. **Crear una nueva solicitud**:
    - Método: **GET**.
    - URL: `http://localhost:8080/accounts`.
3. **Enviar la solicitud**:
    - Haz clic en **Send**.
    - Si todo está correcto, recibirás una respuesta con código **200 OK** y un JSON con la lista de todas las cuentas.

   Ejemplo de respuesta:

   ```json
   [
     {
       "id": 1,
       "name": "Alice",
       "amount": 500.00
     },
     {
       "id": 2,
       "name": "Bob",
       "amount": 700.00
     }
   ]
   ```

#### **Caso 2: Filtrar cuentas por nombre**

1. **Abrir Postman**.
2. **Crear una nueva solicitud**:
    - Método: **GET**.
    - URL: `http://localhost:8080/accounts?name=Alice`.
3. **Enviar la solicitud**:
    - Haz clic en **Send**.
    - Si todo está correcto, recibirás una respuesta con código **200 OK** y un JSON con las cuentas que coinciden con el nombre "Alice".

   Ejemplo de respuesta:

   ```json
   [
     {
       "id": 1,
       "name": "Alice",
       "amount": 500.00
     }
   ]
   ```

---

### **4. Ejemplo de Prueba Completa**

#### **Paso 1: Verificar cuentas antes de la transferencia**

1. Realiza una solicitud **GET** a `http://localhost:8080/accounts`.
2. Anota los saldos de las cuentas con IDs 1 y 2.

#### **Paso 2: Realizar una transferencia**

1. Realiza una solicitud **POST** a `http://localhost:8080/transfer` con el siguiente cuerpo:

   ```json
   {
     "senderAccountId": 1,
     "receiverAccountId": 2,
     "amount": 100.00
   }
   ```

2. Verifica que la respuesta sea **200 OK**.

#### **Paso 3: Verificar cuentas después de la transferencia**

1. Realiza otra solicitud **GET** a `http://localhost:8080/accounts`.
2. Confirma que los saldos de las cuentas con IDs 1 y 2 se hayan actualizado correctamente.

---

### **5. Posibles Errores y Soluciones**

#### **Error 404 (Not Found)**:
- Asegúrate de que la URL sea correcta y que la aplicación esté en ejecución.
- Verifica que los IDs de las cuentas existan en la base de datos.

#### **Error 400 (Bad Request)**:
- Revisa que el JSON enviado en el cuerpo de la solicitud **POST** esté bien formado.
- Asegúrate de que los campos `senderAccountId`, `receiverAccountId` y `amount` estén presentes y tengan valores válidos.

#### **Error 500 (Internal Server Error)**:
- Revisa los logs de la aplicación para identificar el problema.
- Asegúrate de que la base de datos esté configurada correctamente y que las tablas existan.

---

### **Resumen**

- Usa **POST** en `http://localhost:8080/transfer` para realizar transferencias.
- Usa **GET** en `http://localhost:8080/accounts` para obtener todas las cuentas o filtrarlas por nombre.
- Verifica los saldos antes y después de las transferencias para confirmar que todo funciona correctamente.

Con estos pasos, podrás probar y validar el funcionamiento de la aplicación en Postman. ¡Es una excelente manera de asegurarte de que todo esté funcionando como se espera! 😊