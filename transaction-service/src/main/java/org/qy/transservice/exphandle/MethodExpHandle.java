package org.qy.transservice.exphandle;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.qy.transsdk.common.Result;
import org.qy.transsdk.common.ResultCodeEnum;
import org.qy.transsdk.exception.BusinessException;
import org.qy.transsdk.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * 全局异常捕获及处理,配合多语言提示
 *
 * @author qinyang
 * @date 2025/6/5 15:31
 */
@Component
public class MethodExpHandle implements Ordered, HandlerExceptionResolver {
    Logger logger = Logger.INSTANCE;

    @Autowired
    ResourceBundleReader resourceBundleReader;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+100;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        Result result = new Result();
        result.setSuccess(false);

        logger.debug("catched exception: [{}] and dealing, details are below => ",ex.getClass().getSimpleName());
        ex.printStackTrace();

        if(ex instanceof BusinessException) {
            ResultCodeEnum code = ((BusinessException) ex).getCode();
            result.setFailCode(ResultCodeEnum.getCode(code));
            result.setMessage(resourceBundleReader.getMsg(code));

        }else if(ex instanceof DataAccessException){
            result.setFailCode(ResultCodeEnum.getCode(ResultCodeEnum.fail_db));
            result.setMessage(ex.getMessage());
        }else {
            result.setFailCode(ResultCodeEnum.getCode(ResultCodeEnum.fail_other));
            result.setMessage(ex.getMessage());
        }

        if(!ObjectUtils.isEmpty(result)){
            response.setContentType("application/json;charset=UTF-8");
            try {
                response.getWriter().print(JSON.toJSONString(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ModelAndView();
        }

        return null;
    }
}
