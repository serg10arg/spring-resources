package aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

@Aspect
public class SecurityAspect {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(SecurityAspect.class);
    private Logger logger = Logger.getLogger(SecurityAspect.class.getName());

    @Around(value = "@annotation(ToLog)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Security aspect: Calling the intercepted method");
        Object returnedValue = joinPoint.proceed();
        logger.info("Security aspect: Method executed and returned " + returnedValue);
        return returnedValue;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
