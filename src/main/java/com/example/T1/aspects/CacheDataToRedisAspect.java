package com.example.T1.aspects;

import com.example.T1.annotations.Cached;
import com.example.T1.component.RedisCacheUtils;
import com.example.T1.services.RedisCacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class CacheDataToRedisAspect {
    private Logger logger = LoggerFactory.getLogger(CacheDataToRedisAspect.class);
    private final RedisCacheService redisCacheService;

    public CacheDataToRedisAspect(RedisCacheService redisCacheService){
        this.redisCacheService = redisCacheService;
    }


    @Around("@annotation(com.example.T1.annotations.Cached)")
    public Object cachedNotashka(ProceedingJoinPoint joinPoint, Cached cached) throws Throwable{
        logger.info("Аспект перехватил метод.");

        if (cached == null) {
            logger.error("Аннотация @Cached не найдена!");
            return joinPoint.proceed();
        }

        try{
            return redisCacheService.checkCaches(joinPoint, cached);
        } catch(Exception ex){
            logger.error("Ошибка при записи в кеш: {}", ex.getMessage(), ex);
            return joinPoint.proceed();
        }
    }

}