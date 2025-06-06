package org.qy.transsdk.exception;

/**
 * 交易系统最顶级异常，以此为根，构建异常树体系
 *
 * @author qinyang
 * @date 2025/6/5 11:14
 */
public class BaseException extends RuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
