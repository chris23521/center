package com.ncarzone.data.simple.center.facade.model;

public enum ResultSetCode {
	
	SUCCESS(1000, "success"), 
	PARAM_ERROR(2001, "parameter error"), 
	SYSTEM_ERROR(3001, "system error"),
	REQUEST_ERROR_INVALID(4001, "invalid request"),
	UNKNOWN_ERROR(5001, "unknown error"),
	EXIST(6001,"exist"),
	PASSWORD_ERROR(6002,"password error"),
	NOT_EXIST(6003,"not exist"),
	HTTP_EXCEPTION(7000, "http exception")

	;
	
	int code;
	String message;
	
	ResultSetCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}

