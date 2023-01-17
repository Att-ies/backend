package com.sptp.backend.aop.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("execution(* *..*Controller.*(..))")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("==================");
        log.info("[log] Controller={}, Method={}", joinPoint.getSignature().getDeclaringType().getName(), joinPoint.getSignature().getName());

        Long startTimeMs = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        Long endTimeMs = System.currentTimeMillis();

        Long resultTimeMs = endTimeMs - startTimeMs;

        log.info("[log] Execution Time = {}ms", resultTimeMs);
        log.info("==================");
        return result;
    }
}
