package cn.edu.sicau.pfdistribution.service;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * @author 谭华波
 * 2019/5/22
 */
@Component
@Aspect
public class DistributionAspects {

    @Pointcut("execution(public * cn.edu.sicau.pfdistribution.service.kspdistribution.MainDistribution..*.*(..))")
    public void interceptor(){}

    /*返回通知(@AfterReturn)： 在目标方法正常放回之后执行*/
    @AfterReturning(value = "interceptor()", returning = "result")
    public void KafkaInterceptor(JoinPoint joinPoint, Object result) {
        System.out.println("distribution test+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

    @AfterReturning(value = "interceptor()", returning = "result")
    public void mysqlInterceptor(JoinPoint joinPoint, Object result) {
        System.out.println(joinPoint.getSignature().getName() + "正常结束，结果是: {" + result + "}");
    }

    @Before(value = "interceptor()")
    public void logStart(JoinPoint joinPoint) {
        System.out
                .println(joinPoint.getSignature().getName() + "运行，参数列表是: {" + Arrays.asList(joinPoint.getArgs()) + "}");
    }

    @After(value = "interceptor()")
    public void logEnd(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getName() + "结束...");
    }

    @AfterThrowing(value = "interceptor()", throwing = "e")
    public void logException(JoinPoint joinPoint, Exception e) {
        System.out.println(joinPoint.getSignature().getName() + "异常，异常信息: {" + e.getMessage() + "}");
    }

}
