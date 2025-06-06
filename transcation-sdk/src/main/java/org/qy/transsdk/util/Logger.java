package org.qy.transsdk.util;

import org.slf4j.LoggerFactory;

/**
 * 日志统一封装
 *
 * @author qinyang
 * @date 2025/6/5 15:25
 */
public enum Logger {
    INSTANCE;

    private String getClassName(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for(StackTraceElement element : stackTrace){
            if(Thread.class.getName().equals(element.getClassName()) ||
                    this.getClass().getName().equals(element.getClassName())){
                continue;
            }
            return element.getClassName();
        }
        return null;
    }

    private org.slf4j.Logger getLogger(){
        return LoggerFactory.getLogger(getClassName());
    }

    public void info(String msg){
        org.slf4j.Logger logger = getLogger();
        logger.info(msg);
    }

    public void info(String msg,Object... args){
        org.slf4j.Logger logger = getLogger();
        logger.info(msg,args);
    }

    public void debug(String msg){
        org.slf4j.Logger logger = getLogger();
        logger.debug(msg);
    }

    public void debug(String msg,Object... args){
        org.slf4j.Logger logger = getLogger();
        logger.debug(msg,args);
    }

    public void error(String msg){
        org.slf4j.Logger logger = getLogger();
        logger.error(msg);
    }

    public void error(String msg,Object... args){
        org.slf4j.Logger logger = getLogger();
        logger.error(msg,args);
    }
}
