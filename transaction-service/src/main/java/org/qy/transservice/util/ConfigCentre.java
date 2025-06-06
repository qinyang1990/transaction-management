package org.qy.transservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 *
 * 封装 配置读取
 *
 * @author qinyang
 * @date 2025/6/5 20:47
 */
@Component
public class ConfigCentre {

    @Autowired
    Environment environment;

    public String getValue(String key) {
        return environment.getProperty(key);
    }

    public String getValue(String key,String defaultValue) {
        String value = getValue(key);
        if(ObjectUtils.isEmpty(value) && !ObjectUtils.isEmpty(defaultValue)) {
            value = defaultValue;
        }

        return value;
    }

}
