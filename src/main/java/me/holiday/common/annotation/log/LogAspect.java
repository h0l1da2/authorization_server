package me.holiday.common.annotation.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {
    @Pointcut("@annotation(logExecution)")
    public void logPointcut(LogExecution logExecution) {}

    @AfterReturning(pointcut = "logPointcut(logExecution)", returning = "result")
    public void logAfterExecution(JoinPoint joinPoint, LogExecution logExecution, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String logMessage = logExecution.message();

        log.info("[{}] 성공 - {}, {}",
                methodName,
                logMessage,
                result);
    }
}
