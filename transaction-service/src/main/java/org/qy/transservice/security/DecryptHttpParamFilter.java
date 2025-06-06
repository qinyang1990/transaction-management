package org.qy.transservice.security;

import jakarta.servlet.*;
import org.qy.transsdk.util.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 *
 * 模拟所有接口在收到参数前，先经过全局的解密，调用方与服务端加密内容再传输有助于安全
 *
 * @author qinyang
 * @date 2025/6/5 21:53
 */
@Configuration
public class DecryptHttpParamFilter {
    @Bean
    public FilterRegistrationBean<DecryptFilter> decryptFilterRegistration() {
        FilterRegistrationBean<DecryptFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new DecryptFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}

class DecryptFilter implements Filter{
    Logger logger = Logger.INSTANCE;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        logger.info("begin decrypt http params");

        chain.doFilter(request, response);
    }
}