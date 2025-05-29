package com.example.T1.aspects;

import com.example.T1.services.TimeLimitExceedLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class TimeLimitExceedLogAspect {
    private final TimeLimitExceedLogService timeLimitExceedLogService;
    private Logger logger = LoggerFactory.getLogger(TimeLimitExceedLogAspect.class);

    public TimeLimitExceedLogAspect(TimeLimitExceedLogService timeLimitExceedLogService){
        this.timeLimitExceedLogService = timeLimitExceedLogService;
    }

    @Around("@annotation(com.example.T1.annotations.Metric)")
    public Object checkMethodRuningTime(ProceedingJoinPoint joinPoint) throws Throwable{
        logger.info("Аспект сканирует метод {}", joinPoint.getSignature().toShortString());
        Long startTime = System.currentTimeMillis();
        try{
            return joinPoint.proceed();
        } finally{
            Long endTime = System.currentTimeMillis();
            Long duration = endTime - startTime;

            logger.info("Метод {} выполнился за {} мс",
                    joinPoint.getSignature().toShortString(),
                    duration);

            timeLimitExceedLogService.checkSlowMethod(joinPoint.getSignature().toShortString(),
                    duration);
            logger.info("Аспект отработал.");
        }
    }

}