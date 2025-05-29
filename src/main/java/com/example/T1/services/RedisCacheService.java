package com.example.T1.services;

import com.example.T1.annotations.Cached;
import com.example.T1.component.RedisCacheUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@Service
public class RedisCacheService {
    private Logger logger = LoggerFactory.getLogger(RedisCacheService.class);
    private final RedisCacheUtils redisCacheUtils;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Value("${spring.cache.redis.time-to-lived}")
    private Long limitTime;

    public RedisCacheService(RedisCacheUtils redisCacheUtils){
        this.redisCacheUtils = redisCacheUtils;
    }

    public Object checkCaches(ProceedingJoinPoint joinPoint, Cached cached) throws Throwable{
        String cachedName = cached.cacheName();
        String keyExpression = cached.key();

        //Ключ из Spel
        String key = generateKey(joinPoint, keyExpression);

        String fullKey = cachedName + "::" + key;

        logger.info("Получен объект кэша: {}", fullKey);

        if (redisCacheUtils.hasKey(fullKey)){
            logger.info("Объект есть в кеше.");
            return redisCacheUtils.getValue(fullKey);
        }

        //Если кеша нет
        Object result = joinPoint.proceed();
        redisCacheUtils.putValue(fullKey, result, limitTime);

        logger.info("Объект был кеширован.");

        return result;

    }

    private String generateKey(ProceedingJoinPoint joinPoint, String keyExpression){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        StandardEvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(keyExpression).getValue(context, String.class);
    }
}