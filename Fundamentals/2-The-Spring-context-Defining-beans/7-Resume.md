## **Resumen**

- Lo primero que necesitas aprender en Spring es agregar instancias de objetos (que llamamos beans) al contexto de Spring. Puedes imaginar el contexto de Spring como un cubo en el que agregas las instancias que esperas que Spring pueda gestionar. Spring solo puede ver las instancias que agregas a su contexto.
- Puedes agregar beans al contexto de Spring de tres maneras: usando la anotación @Bean, usando anotaciones de estereotipo y haciéndolo programáticamente.
    - Usar la anotación @Bean para agregar instancias al contexto de Spring te permite agregar cualquier tipo de instancia de objeto como un bean e incluso múltiples instancias del mismo tipo al contexto de Spring. Desde este punto de vista, este enfoque es más flexible que usar anotaciones de estereotipo. Sin embargo, requiere que escribas más código porque necesitas escribir un método separado en la clase de configuración para cada instancia independiente agregada al contexto.
    - Usar anotaciones de estereotipo te permite crear beans solo para las clases de la aplicación con una anotación específica (por ejemplo, @Component). Este enfoque de configuración requiere escribir menos código, lo que hace que tu configuración sea más fácil de leer. Preferirás este enfoque sobre la anotación @Bean para las clases que defines y puedes anotar.
    - Usar el método registerBean() te permite implementar lógica personalizada para agregar beans al contexto de Spring. Recuerda, puedes usar este enfoque solo con Spring 5 y versiones posteriores.

