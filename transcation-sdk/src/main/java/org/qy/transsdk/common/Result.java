package org.qy.transsdk.common;

/**
 * 接口返回的公共信息体
 * @author qinyang
 * @date 2025/6/5 10:51
 */
public class Result<T> {
    Boolean success = true;
    Integer failCode;
    String message;
    T data;

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public static <T> Result buildSuccess(T data) {
        Result result = new Result(data);
        result.setMessage("请求成功");
        return result;
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getFailCode() {
        return failCode;
    }

    public void setFailCode(Integer failCode) {
        this.failCode = failCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
