package cn.cas.ntsc.util.guava.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    int NOT_LIMITED = 0;

    double qps() default NOT_LIMITED;

    int timeout() default 0;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
