package me.xueyao.response;

import java.io.Serializable;

public class BaseResponse implements Serializable {
	private Integer code;
	private String msg;
	private Object data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setStatus(ResponseStatus status) {
		this.code = status.code();
		this.msg = status.msg();
	}

	public BaseResponse(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public BaseResponse(ResponseStatus status) {
		this.code = status.code();
		this.msg = status.msg();
	}

	public BaseResponse() {
	}
}
