package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Aspect
@Log4j2
@Component
public class LogAdvice {
	@Before( "execution(* org.zerock.service.SampleService*.*(..))")
	//execution : 접근제한자와 특정 클래스의 메서드 지정
	//* : 접근 제한자(맨앞) / 클래스의 이름과 메서드의 이름(맨뒤)
	public void logBefore() {
		log.info("===============================");
	}
	
	@Before( "execution(* org.zerock.service.SampleService*.doAdd(String, String)) && args(str1, str2)")
	public void logBeforeWithParam(String str1, String str2) {
		log.info("str1 : " + str1);
		log.info("str2 : " + str2);
	}
	
	@AfterThrowing(pointcut = "execution(* org.zerock.service.SampleService*.*(..))", throwing="exception")
	// AfterThrowing 어노테이션은 지정된 대상이 예외를 발생한 후에 동작 -> 문제를 찾을 수 있도록 도움
	public void logException(Exception exception) {
		log.info("Exception...!");
		log.info("exception : " + exception);
	}
	
	@Around("execution(* org.zerock.service.SampleService*.*(..))")
	// Around 어노테이션의 동작 : 직접 대상 메서드를 실행할 수 있는 권한을 가지고 있고, 메서드의 실행 전과 실행 후에 처리가 가능
	// AOP를 이용한 구체적인 처리 -> @Around, ProceedingJoinPoint
	public Object logTime( ProceedingJoinPoint pjp) {
		// logTime의 파라미터 : ProceedingJoinPoint
		// ProceedingJoinPoint는 AOP의 대상이 되는 Target이나 파라미터 등을 파악, 직접 실행을 결정
		// Around 어노테이션이 적용되는 메서드의 경우에는 리턴 타입이 void가 아닌 타입으로 설정
		// 메서드의 실행 결과 역시 직접 반환하는 형태로 작성
		long start = System.currentTimeMillis();
		
		log.info("Target : " + pjp.getTarget());
		log.info("Param : " + Arrays.toString(pjp.getArgs()));
		
		// invoke method
		Object result = null;
		
		try {
			result = pjp.proceed();
		} catch(Throwable e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		
		log.info("TIME : " + (end - start));
		
		return result;
		// 실행 결과 : @Around 동작 후 @Before등이 실행된 후에 메서드가 실행되는데 걸린 시간이 로그로 기록
	}
}
