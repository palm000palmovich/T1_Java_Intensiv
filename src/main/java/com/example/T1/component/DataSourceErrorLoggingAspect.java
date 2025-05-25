package com.example.T1.component;

import com.example.T1.model.DataSourceErrorLog;
import com.example.T1.repository.DataSourceErrorLogRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class DataSourceErrorLoggingAspect {
    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;
    private Logger logger = LoggerFactory.getLogger(DataSourceErrorLoggingAspect.class);

    public DataSourceErrorLoggingAspect(DataSourceErrorLogRepository dataSourceErrorLogRepository){
        this.dataSourceErrorLogRepository = dataSourceErrorLogRepository;
    }


    @Around("@annotation(com.example.T1.annotations.LogDataSourceError)")
    public Object logDataSourceError(ProceedingJoinPoint joinPoint) throws Throwable{
        try{
            return joinPoint.proceed();
        } catch (Throwable ex){
            logger.error("Аспект перехватил исключение: {}", ex.getMessage());
            DataSourceErrorLog errorLog = new DataSourceErrorLog();
            errorLog.setStackTrace(ExceptionUtils.getStackTrace(ex));
            errorLog.setMessage(ex.getMessage());
            errorLog.setMethodSignature(joinPoint.getSignature().toShortString());
            dataSourceErrorLogRepository.save(errorLog);
            logger.info("Запись DataSourceErrorLog сохранена: {}", errorLog);
            return null;
        }

    }

}