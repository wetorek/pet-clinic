package com.clinic.pet.petclinic.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("within(com.clinic.pet.petclinic.controller.rest..*)")
    public void restControllerPackagePointcut() {
    }

    @Pointcut("within(com.clinic.pet.petclinic.controller.thyme..*)")
    public void thymeControllerPackagePointcut() {
    }

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerMethodsPointcut() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethodsPointcut() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestControllerAdvice *)")
    public void restControllerExceptionAdvice() {
    }

    @Around("restControllerPackagePointcut() && restControllerMethodsPointcut()")
    public Object logAroundRestController(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint);
    }

    @Around("thymeControllerPackagePointcut() && controllerMethodsPointcut()")
    public Object logAroundThymeController(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint);
    }

    @Around("restControllerPackagePointcut() && restControllerExceptionAdvice()")
    public Object logExceptionAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Exception thrown: {} and handled in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        log.info("Returning error response: {}", result);
        return result;
    }

    private Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result);
            return result;
        } catch (Exception e) {
            log.error("Exception thrown for: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
