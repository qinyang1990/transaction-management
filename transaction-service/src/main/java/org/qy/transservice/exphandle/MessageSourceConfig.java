package org.qy.transservice.exphandle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 多语言资源的配置
 *
 * @author qinyang
 * @date 2025/6/5 14:47
 */
@Configuration
public class MessageSourceConfig {

    private final String resourceBundlePrefix = "hint";

    @Bean
    public ResourceBundleMessageSource messageSource(){
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames(resourceBundlePrefix);
        source.setDefaultEncoding("UTF-8");
        return source;
    }

}
