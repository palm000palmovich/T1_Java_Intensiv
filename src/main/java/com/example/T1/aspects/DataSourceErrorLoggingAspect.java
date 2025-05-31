package com.example.T1.aspects;

import com.example.T1.exceptions.UserNotFoundException;
import com.example.T1.kafka.KafkaSender;
import com.example.T1.services.DataSourseErrorLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Aspect
@Component
public class DataSourceErrorLoggingAspect {
    private final DataSourseErrorLogService dataSourseErrorLogService;
    private final KafkaSender kafkaSender;
    private Logger logger = LoggerFactory.getLogger(DataSourceErrorLoggingAspect.class);

    public DataSourceErrorLoggingAspect(DataSourseErrorLogService dataSourseErrorLogService,
                                        KafkaSender kafkaSender){
        this.dataSourseErrorLogService = dataSourseErrorLogService;
        this.kafkaSender = kafkaSender;
    }


    @Around("@annotation(com.example.T1.annotations.LogDataSourceError)")
    public Object logDataSourceError(ProceedingJoinPoint joinPoint) throws Throwable{
        try{
            return joinPoint.proceed();
        } catch (UserNotFoundException ex){
            logger.error("Аспект перехватил исключение: {}", ex.getMessage());

            try{
                kafkaSender.sendMessage("t1_demo_metrics",
                        joinPoint.getSignature().toShortString(),
                        ex.getMessage(),
                        "DATA_SOURCE");

                logger.info("Сообщение успешно отправлено из аспекта.");
            } catch (Exception exep){
                logger.error("Проблема с отправкой сообщения в кафку.");
                dataSourseErrorLogService.saveErrorToBd(ex, joinPoint.getSignature().toShortString());
            }

            logger.info("Аспект отработал.");

            throw ex;
        }

    }

}