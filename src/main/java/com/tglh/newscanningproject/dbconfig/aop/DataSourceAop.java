package com.tglh.newscanningproject.dbconfig.aop;

import com.tglh.newscanningproject.dbconfig.bean.DBContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAop {

    @Pointcut("!@annotation(com.tglh.newscanningproject.dbconfig.annotation.Master) " +
            "&& (execution(* com.tglh.newscanningproject.scanning.service..*.select*(..)) " +
            "|| execution(* com.tglh.newscanningproject.scanning.service..*.get*(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(com.tglh.newscanningproject.dbconfig.annotation.Master) " +
            "|| execution(* com.tglh.newscanningproject.scanning.service..*.insert*(..)) " +
            "|| execution(* com.tglh.newscanningproject.scanning.service..*.add*(..)) " +
            "|| execution(* com.tglh.newscanningproject.scanning.service..*.update*(..)) " +
            "|| execution(* com.tglh.newscanningproject.scanning.service..*.edit*(..)) " +
            "|| execution(* com.tglh.newscanningproject.scanning.service..*.delete*(..)) " +
            "|| execution(* com.tglh.newscanningproject.scanning.service..*.remove*(..))")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }

}
