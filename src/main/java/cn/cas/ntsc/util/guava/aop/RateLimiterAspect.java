package cn.cas.ntsc.util.guava.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.cas.ntsc.util.guava.annotation.RateLimiter;

@Slf4j
@Aspect
@Component
public class RateLimiterAspect {
    private static final ConcurrentMap<String, com.google.common.util.concurrent.RateLimiter> rateLimiterCache = new ConcurrentHashMap<>();

    @Pointcut("@annotation(cn.cas.ntsc.util.guava.annotation.RateLimiter)")
    public void rateLimit() {
    }

    @Around("rateLimit()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        if (rateLimiter != null && rateLimiter.qps() > RateLimiter.NOT_LIMITED) {
            double qps = rateLimiter.qps();
            if (rateLimiterCache.get(method.getName()) == null) {
                rateLimiterCache.put(method.getName(), com.google.common.util.concurrent.RateLimiter.create(qps));
            }
            log.info("{} qps initial to {}", method.getName(), rateLimiterCache.get(method.getName()).getRate());
            if (rateLimiterCache.get(method.getName()) != null && !rateLimiterCache.get(method.getName()).tryAcquire(rateLimiter.timeout(), rateLimiter.timeUnit())) {
                throw new RuntimeException("rate limit trigger");
            }
        }
        return point.proceed();
    }
}
