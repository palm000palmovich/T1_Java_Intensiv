package com.example.T1.aspects;

import com.example.T1.model.DataSourceErrorLog;
import com.example.T1.repository.DataSourceErrorLogRepository;
import com.example.T1.services.DataSourseErrorLogService;
import org.apache.commons.lang3.exception.ExceptionUtils;


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
    private Logger logger = LoggerFactory.getLogger(DataSourceErrorLoggingAspect.class);

    public DataSourceErrorLoggingAspect(DataSourseErrorLogService dataSourseErrorLogService){
        this.dataSourseErrorLogService = dataSourseErrorLogService;
    }


    @Around("@annotation(com.example.T1.annotations.LogDataSourceError)")
    public Object logDataSourceError(ProceedingJoinPoint joinPoint) throws Throwable{
        try{
            return joinPoint.proceed();
        } catch (Exception ex){
            logger.error("Аспект перехватил исключение: {}", ex.getMessage());
            dataSourseErrorLogService.saveErrorToBd(ex, joinPoint.getSignature().toShortString());
            logger.info("Аспект отработал.");

            throw ex;
        }

    }

}