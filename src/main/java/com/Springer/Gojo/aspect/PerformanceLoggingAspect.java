package com.Springer.Gojo.aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class PerformanceLoggingAspect {

	@Around("execution(* com.Springer.Gojo.service.impl.*.*(..)) || execution(* com.Springer.Gojo.contoller.*.*(..))")
	public Object logPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.stop();
		
		log.atDebug()
			.setMessage("Entering method")
			.addKeyValue("class",className)
			.addKeyValue("method", methodName)
			.log();
		
		try {
			Object result = joinPoint.proceed();
			
			stopWatch.stop();
			
			log.atInfo()
				.setMessage("Method exection completed")
				.addKeyValue("class", className)
				.addKeyValue("method", methodName)
				.addKeyValue("durationMs", stopWatch.getTotalTimeMillis())
				.log();
			
			return result;
			
		}catch(Throwable ex) {
			stopWatch.stop();
			log.atError()
				.setMessage("Method failed")
				.addKeyValue("class", className)
	               .addKeyValue("method", methodName)
	               .addKeyValue("durationMs", stopWatch.getTotalTimeMillis())
	               .setCause(ex)
	               .log();
	           throw ex;
		}
	}
}
