package com.sc.mp.annotation.aspect;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.sc.mp.annotation.OperationLog;
import com.sc.mp.annotation.bean.OperationLogBean;
import com.sc.mp.model.WebScUser;
import com.sc.mp.util.ArrayUtil;
import com.sc.mp.util.RequestSessionUtil;

@Component
@Aspect
public class LogAspect {
	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	/**
     * 此处的切点是注解的方式，也可以用包名的方式达到相同的效果
     * '@Pointcut("execution(* com.wwj.springboot.service.impl.*.*(..))")'
     */
    @Pointcut("@annotation(com.sc.mp.annotation.OperationLog)")
    public void operationLog(){}
    
    /**
     * 环绕增强，相当于MethodInterceptor
     */
    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        long time = System.currentTimeMillis();
        try {
            res =  joinPoint.proceed();
            time = System.currentTimeMillis() - time;
            return res;
        } finally {
            try {
                //方法执行完成后增加日志
                addOperationLog(joinPoint,res,time);
            }catch (Exception e){
            	logger.error("LogAspect 操作失败：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 打印操作log
     * @param joinPoint
     * @param res
     * @param time
     */
    private void addOperationLog(JoinPoint joinPoint, Object res, long time){
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        OperationLogBean operationLog = new OperationLogBean();
        operationLog.setRunTime(time);
        operationLog.setReturnValue(res.toString());
        operationLog.setId(UUID.randomUUID().toString());
        if (joinPoint.getArgs().length == 0) {
        	operationLog.setArgs("[]");
		}else {
			List<Object> args = ArrayUtil.toList(joinPoint.getArgs());
			for (int i = 0; i < args.size(); i++) {
				if (args.get(i) instanceof HttpServletRequest) {
					args.remove(i);
				}
			}
			operationLog.setArgs(JSONArray.toJSONString(args).toString());
		}
        operationLog.setCreateDate(new Date());
        operationLog.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        WebScUser user = (WebScUser) RequestSessionUtil.getCurUser();
        if(user != null) {
        	operationLog.setUserId(user.getUserId()+"");
        	operationLog.setUserName(user.getLoginName());
        }
        OperationLog annotation = signature.getMethod().getAnnotation(OperationLog.class);
        if(annotation != null){
            operationLog.setDescribe(annotation.value());
        }
        
        logger.info("操作日志:" + operationLog.toString());
    }
    
    @Before("operationLog()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        System.out.println("进入方法前执行.....");

    }

    /**
     * 处理完请求，返回内容
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "operationLog()")
    public void doAfterReturning(Object ret) {
        System.out.println("方法的返回值 : " + ret);
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing("operationLog()")
    public void throwss(JoinPoint jp){
        System.out.println("方法异常时执行.....");
    }


    /**
     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
     */
    @After("operationLog()")
    public void after(JoinPoint jp){
        System.out.println("方法最后执行.....");
    }

}
