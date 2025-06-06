package org.qy.transsdk.exception;


/**
 *
 * @author qinyang
 * @date 2025/6/5 11:23
 */
public class RpcException extends BaseException {

    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

}
