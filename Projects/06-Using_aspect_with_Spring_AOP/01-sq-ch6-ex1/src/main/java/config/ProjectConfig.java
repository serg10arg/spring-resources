package config;

import aspect.LoggingAspect;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"services"})
@EnableAspectJAutoProxy
public class ProjectConfig {

    @Bean
    public LoggingAspect aspect() {
        return new LoggingAspect();
    }
}
