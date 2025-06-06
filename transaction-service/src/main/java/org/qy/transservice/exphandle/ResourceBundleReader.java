package org.qy.transservice.exphandle;

import org.qy.transsdk.common.ResultCodeEnum;
import org.qy.transservice.util.ConfigCentre;
import org.qy.transservice.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 多语言具体内容读取,根据当前语言环境配置
 *
 * @author qinyang
 * @date 2025/6/5 15:52
 */
@Component
public class ResourceBundleReader implements ApplicationRunner {

    @Autowired
    ConfigCentre configCentre;

    Locale locale = null;

    private final String localeKey = "lang";

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String lang = configCentre.getValue(localeKey);
        locale = new Locale(lang);
    }

    public String getMsg(ResultCodeEnum code) {
        MessageSource messageSource = SpringContextUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(code.toString(), null, locale);
        return message;
    }


}
