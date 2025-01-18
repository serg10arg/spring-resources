# Resumen

- El **contexto de Spring** es un lugar en la memoria de la aplicación que el framework utiliza para mantener los objetos que gestiona. Necesitas agregar cualquier objeto que necesite ser mejorado con una característica que ofrece el framework al contexto de Spring. 🌱

- Al implementar una aplicación, necesitas referirte de un objeto a otro. De esta manera, un objeto puede delegar acciones a otros objetos al ejecutar sus responsabilidades. Para implementar este comportamiento, necesitas establecer **relaciones entre los beans** en el contexto de Spring. 🔗

- Puedes establecer una relación entre dos beans usando uno de tres enfoques:
    - **Refiriéndote directamente al método anotado con `@Bean`** que crea uno de ellos desde el método que crea el otro. Spring sabe que te refieres al bean en el contexto, y si el bean ya existe, no llama al mismo método nuevamente para crear otra instancia. En su lugar, devuelve la referencia al bean existente en el contexto. 🛠️
    - **Definiendo un parámetro para el método anotado con `@Bean`**. Cuando Spring observa que el método `@Bean` tiene un parámetro, busca un bean de ese tipo de parámetro en su contexto y proporciona ese bean como valor para el parámetro. 🧩
    - **Usando la anotación `@Autowired`** de tres maneras:
        - **Anotar el campo** en la clase donde deseas instruir a Spring para que inyecte el bean desde el contexto. Encontrarás este enfoque a menudo utilizado en ejemplos y pruebas de concepto. 📋
        - **Anotar el constructor** que deseas que Spring llame para crear el bean. Spring inyectará otros beans desde el contexto en los parámetros del constructor. Este es el enfoque más utilizado en código del mundo real. 💼
        - **Anotar el setter del atributo** donde deseas que Spring inyecte el bean desde el contexto. Este enfoque no se utiliza tan frecuentemente en código listo para producción. ⚙️

- Siempre que permites que Spring proporcione un valor o referencia a través de un atributo de la clase o un parámetro de método o constructor, decimos que Spring usa **DI (Inyección de Dependencias)**, una técnica respaldada por el principio de **IoC (Inversión de Control)**. 🔄

- La **creación de dos beans que dependen uno del otro** genera una dependencia circular. Spring no puede crear los beans con una dependencia circular, y la ejecución falla con una excepción. Al configurar tus beans, asegúrate de evitar dependencias circulares. 🚫

- Cuando Spring tiene más de un bean del mismo tipo en su contexto, no puede decidir cuál de esos beans necesita ser inyectado. Puedes decirle a Spring cuál es la instancia que necesita inyectar usando:
    - La anotación `@Primary`, que marca uno de los beans como el predeterminado para la inyección de dependencias, o
    - Nombrando los beans e inyectándolos por nombre usando la anotación `@Qualifier`. 🎯
