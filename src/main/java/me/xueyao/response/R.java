package me.xueyao.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Simon.Xue
 * @date 2020-03-03 20:26
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class R<T> {
    /**
     * 请求成功
     */
    public static final int CODE_SUCCESS = 200;

    /**
     * 系统异常
     */
    public static final int CODE_SYS_ERROR = 500;

    /**
     * 参数错误
     */
    public static final int CODE_PARAMS_ERROR = 400;

    /**
     * 超时错误，如：登陆超时、授权超时等；
     */
    public static final int CODE_TIME_OUT = 401;

    /**
     * 用户不存在错误，必要情况跳转登陆页面；
     */
    public static final int CODE_NULL_USER = 402;

    /**
     * 用户权限不匹配，无操作权限；
     */
    public static final int CODE_AUTHORITY_ERROR = 403;
    /**
     * 系统异常 提示信息
     */
    public static final String MESSAGE_SYS_ERROR = "系统异常";

    /**
     * 操作成功提示信息
     */
    public static final String MESSAGE_SUCCESS = "操作成功";
    private Integer code;
    private String message;
    private T data;

    public R(String msg, T object) {
        this.message = msg;
        this.data = object;
    }

    public R(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }


    public T getData() {
        return this.data;
    }


    public static <T> R<T> ofParam(String msg) {
        return new R(CODE_PARAMS_ERROR, msg);
    }

    public static <T> R<T> ofParam(T data, String msg) {
        R r = new R();
        r.setCode(CODE_PARAMS_ERROR);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static <T> R<T> ofSuccess(String msg) {
        return new R(CODE_SUCCESS, msg);
    }

    public static <T> R<T> ofSuccess(String msg, T data) {
        return new R(CODE_SUCCESS, msg, data);
    }

    public static <T> R<T> ofSystem(String msg) {
        return new R(CODE_SYS_ERROR, msg);
    }


}
