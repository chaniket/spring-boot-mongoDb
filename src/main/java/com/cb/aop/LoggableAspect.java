package com.cb.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Aspect
public class LoggableAspect {

//	@Around("@annotation(com.cb.aop.Loggable)")
//	public Object logMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//		log.info("Method execution started class {}, method {} ",joinPoint.getSignature().getDeclaringType().getSimpleName() ,joinPoint.getSignature().getName());
//		Object ratVal = joinPoint.proceed();
//		log.info(joinPoint.getSignature().getName());
//		return ratVal;
//	}
	
	@Around("@annotation(com.cb.aop.Loggable)")
	//@Around("@annotation(com.cb.customannotation.Loggable)")
	public Object logMethodAndTime(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("Method execution started class {}, method {} ",joinPoint.getSignature().getDeclaringType().getSimpleName() ,joinPoint.getSignature().getName());
		Object val = joinPoint.proceed();
		log.info("Method execution ended class {}, method {} ",joinPoint.getSignature().getDeclaringType().getSimpleName() ,joinPoint.getSignature().getName());
		return val;	
	}
	
}
