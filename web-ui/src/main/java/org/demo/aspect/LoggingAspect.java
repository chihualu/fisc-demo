package org.demo.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Log4j2
@Aspect
@Component
@Order(1)
public class LoggingAspect {

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Around(value = "@annotation(org.demo.annotatioon.Logging)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        String name = proceedingJoinPoint.getTarget().getClass().getName();
        String method = proceedingJoinPoint.getSignature().getName();
        log.debug("invoke method {}.{}", name ,method);
        Object ret = proceedingJoinPoint.proceed();
        log.debug("invoke method {}.{} waste {} ms", name ,method, (System.currentTimeMillis()-startTime.get()));
        return ret;
    }

}
