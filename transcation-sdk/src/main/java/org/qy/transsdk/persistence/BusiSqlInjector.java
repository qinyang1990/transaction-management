package org.qy.transsdk.persistence;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.apache.ibatis.session.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 增加批量添加方法
 *
 * @author qinyang
 * @date 2025/6/5 15:07
 */
@Component
public class BusiSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(configuration, mapperClass, tableInfo);
        methodList.add(new InsertBatchSomeColumn(null));
        return methodList;
    }
}
