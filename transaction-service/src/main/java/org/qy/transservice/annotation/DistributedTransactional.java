package org.qy.transservice.annotation;


import java.lang.annotation.*;

/**
 * 分布式事务的标记
 *
 * @author qinyang
 * @date 2025/6/5 18:46
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface DistributedTransactional {

}
