package org.qy.transsdk.exception;

import org.qy.transsdk.common.ResultCodeEnum;

/**
 * 交易系统常用的业务异常
 *
 * @author qinyang
 * @date 2025/6/5 11:16
 */
public class BusinessException extends BaseException {

    ResultCodeEnum code;

    public ResultCodeEnum getCode() {
        return code;
    }

    public BusinessException(ResultCodeEnum code) {
        super();
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
