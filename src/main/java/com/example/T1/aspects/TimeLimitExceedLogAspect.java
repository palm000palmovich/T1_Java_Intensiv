package com.example.T1.aspects;

import com.example.T1.kafka.KafkaSender;
import com.example.T1.services.TimeLimitExceedLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class TimeLimitExceedLogAspect {
    private final TimeLimitExceedLogService timeLimitExceedLogService;
    private final KafkaSender kafkaSender;
    private Logger logger = LoggerFactory.getLogger(TimeLimitExceedLogAspect.class);

    @Value("${limit.method.time}")
    private Long limitTime;

    public TimeLimitExceedLogAspect(TimeLimitExceedLogService timeLimitExceedLogService,
                                    KafkaSender kafkaSender){
        this.timeLimitExceedLogService = timeLimitExceedLogService;
        this.kafkaSender = kafkaSender;
    }

    @Around("@annotation(com.example.T1.annotations.Metric)")
    public Object checkMethodRunningTime(ProceedingJoinPoint joinPoint) throws Throwable{
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

            if (duration > limitTime){
                try{
                    kafkaSender.sendMessage("t1_demo_metrics",
                            joinPoint.getSignature().toShortString(),
                            "Превышение лимита по времени: " + (duration - limitTime),
                            "METRICS");
                    logger.info("Сообщение успешно отправлено из аспекта.");
                } catch(RuntimeException ex){
                    logger.error("Kafka не смогла отправить сообщение");
                    timeLimitExceedLogService.checkSlowMethod(joinPoint.getSignature().toShortString(),
                            duration);
                }
            }

            logger.info("Аспект отработал.");
        }
    }

}