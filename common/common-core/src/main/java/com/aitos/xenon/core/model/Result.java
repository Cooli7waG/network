package com.aitos.xenon.core.model;

import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.ConstantsCode;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private int code;

	@Getter
	@Setter
	private String msg;


	@Getter
	@Setter
	private T data;

	public static <T> Result<T> ok() {
		return restResult(null, ApiStatus.SUCCESS.getCode(), null);
	}

	public static <T> Result<T> ok(T data) {
		return restResult(data, ApiStatus.SUCCESS.getCode(), null);
	}


	public static <T> Result<T> failed() {
		return restResult(null, ApiStatus.FAILED.getCode(), ApiStatus.FAILED.getMsg());
	}

	public static <T> Result<T> failed(String msg) {
		return restResult(null, ApiStatus.FAILED.getCode(), msg);
	}

	public static <T> Result<T> failed(ConstantsCode constantsCode) {
		return restResult(null, constantsCode.getCode(), constantsCode.getMsg());
	}

	public static <T> Result<T> failed(ConstantsCode constantsCode,String message) {
		return restResult(null, constantsCode.getCode(), message);
	}

	private static <T> Result<T> restResult(T data, int code, String msg) {
		Result<T> apiResult = new Result<>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMsg(msg);
		return apiResult;
	}
}