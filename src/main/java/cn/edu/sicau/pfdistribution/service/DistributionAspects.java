package cn.edu.sicau.pfdistribution.service;

import cn.edu.sicau.pfdistribution.service.kafka.sender.KafkaPfAllocationMessageSender;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author 谭华波
 * 2019/5/22
 */
@Aspect
public class DistributionAspects {

    @Autowired
    KafkaPfAllocationMessageSender sender;

    @AfterReturning(value = "execution(* cn.edu.sicau.pfdistribution.service.kspdistribution.MainDistribution.triggerTask())",returning = "result")  /*返回通知(@AfterReturn)： 在目标方法正常放回之后执行*/
    public void mysqlInterceptor(JoinPoint joinPoint, Object result) {
        sender.send("yourkafka",result);
//        System.out.println(joinPoint.getSignature().getName() + "正常结束，结果是: {" + result + "}");

    }

    @AfterReturning(value = "execution(* cn.edu.sicau.pfdistribution.service.kafka.receiver.KafkaPfAllocationCmdReceiver..*.*(..))", returning = "result")  /*返回通知(@AfterReturn)： 在目标方法正常放回之后执行*/
    public void KafkaInterceptor(JoinPoint joinPoint, Object result) {
        System.out.println(joinPoint.getSignature().getName() + "正常结束，结果是: {" + result + "}");

    }




/*    @Before(value = "logging()")   //前置通知(@Before)： 在目标方法运行之前执行
    public void logStart(JoinPoint joinPoint) {
        System.out
                .println(joinPoint.getSignature().getName() + "运行，参数列表是: {" + Arrays.asList(joinPoint.getArgs()) + "}");
    }

    @After(value = "logging()")  *//*后置通知(@After)： 在目标方法运行结束之后执行，不管正常结束还是异常结束，都会执行*//*
    public void logEnd(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getName() + "结束...");
    }



    @AfterThrowing(value = "logging()", throwing = "e")   *//*异常通知(@AfterThrowing)： 在目标方法出现异常以后执行*//*
    public void logException(JoinPoint joinPoint, Exception e) {
        System.out.println(joinPoint.getSignature().getName() + "异常，异常信息: {" + e.getMessage() + "}");
    }*/
}
