package aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.logging.Logger;

@Aspect
public class LoggingAspects {
    private Logger logger = Logger.getLogger(LoggingAspects.class.getName());

    @AfterReturning(value = "@annotation(ToLog)", returning = "returnedValue")
    public void log(Object returnedValue){
        logger.info("Method executed and returned " + returnedValue);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
