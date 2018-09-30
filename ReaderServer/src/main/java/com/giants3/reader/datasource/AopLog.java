package com.giants3.reader.datasource;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Aspect
@Component
public class AopLog {

    @Autowired
    EntityManagerFactory entityManagerFactory;
      
    private static final Logger logger=Logger.getLogger(AopLog.class);
      
    //@After("execution(* com.controller.*.*(..))")  
     public void doAfter(JoinPoint jp) {
//          System.out.println("log Ending method: "    
//                  + jp.getTarget().getClass().getName() + "."    
//                  + jp.getSignature().getName());
            logger.info("log Ending method: "    
                    + jp.getTarget().getClass().getName() + "."    
                    + jp.getSignature().getName());  
           /* LoggerUtil.info(jp.getClass(),jp.getTarget().getClass().getName() + "."   
                    + jp.getSignature().getName()+"[logo]:normal");*/

        }    
    //@Around("execution(* com.controller.*.*(..))")  
        public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
            long time = System.currentTimeMillis();    
            Object retVal = pjp.proceed();    
            time = System.currentTimeMillis() - time;    
//          System.out.println("process time: " + time + " ms");    
            logger.info("process time: " + time + " ms");  
            return retVal;    
        }    
    //@Before("execution(* com.controller.*.*(..))")  
        public void doBefore(JoinPoint jp) throws Exception {

              
             logger.info("log Begining method: "    
                    + jp.getTarget().getClass().getName() + "."    
                    + jp.getSignature().getName());  
//          System.out.println("log Begining method: "    
//                  + jp.getTarget().getClass().getName() + "."    
//                  + jp.getSignature().getName());    
        }    
    //@AfterThrowing("execution(* com.controller.*.*(..))")  
        public void doThrowing(JoinPoint jp, Throwable ex) {    
//          System.out.println("method " + jp.getTarget().getClass().getName()    
//                  + "." + jp.getSignature().getName() + " throw exception");  
            logger.info("method " + jp.getTarget().getClass().getName()    
                    + "." + jp.getSignature().getName() + " throw exception");  
//          System.out.println(ex.getMessage());    
            logger.info(ex.getMessage());  
            /*LoggerUtil.info(jp.getClass(),jp.getTarget().getClass().getName() + "."   
                    + jp.getSignature().getName()+"[logo]:exception"+ex.toString());*/  
        }    
          
       /* private void sendEx(JoinPoint jp,String ex) {   
            LoggerUtil.info(jp.getClass(),jp.getTarget().getClass().getName() + "."   
                    + jp.getSignature().getName()+"[logo]:exception"+ex); 
        }  */  
}  