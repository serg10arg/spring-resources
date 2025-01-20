package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "beans") // Indica que Spring escanee el paquete "beans" para encontrar clases con @Component
public class ProjectConfig {

}
