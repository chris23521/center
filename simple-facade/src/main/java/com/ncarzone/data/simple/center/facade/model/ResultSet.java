package com.ncarzone.data.simple.center.facade.model;


import java.io.Serializable;

/**
 * 统一返回结构体
 */
public class ResultSet<T> implements Serializable {
	
	private static final long serialVersionUID = -5248479456824719563L;

	/** 获取数据是否成功的标识 true是 false否 */
	private boolean success;

	/** 业务code 具体含义见 ResultSetCode枚举类定义 */
    private int code;

    /** 对code的描述 */
    private String message;

    /** 出错时的错误信息 */
    private String tips;

    /** 返回的数据 */
    private T result;

    /** 分页查询是数据总数 */
    private Integer totalNum;

    public ResultSet() {}

    private ResultSet(boolean success, int code, String message, T result, String tips, int totalNum) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.result = result;
        this.tips = tips;
        this.totalNum = totalNum;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public T getResult() {
        return result;
    }

    public void setResult(T t) {
        this.result = t;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public static <T> ResultSet<T> valueOf(ResultSetCode resultSetCode) {
        return valueOf(resultSetCode, null);
    }

    public static <T> ResultSet<T> valueOf(ResultSetCode resultSetCode, String tips) {
        if (resultSetCode == ResultSetCode.SUCCESS) {
            return new ResultSet<T>(true, resultSetCode.getCode(), resultSetCode.getMessage(), null, tips, 0);
        } else {
            return new ResultSet<T>(false, resultSetCode.getCode(), resultSetCode.getMessage(), null, tips, 0);
        }
    }

    public static <T> ResultSet<T> valueOf(ResultSetCode resultSetCode, String tips, T result) {
        if (resultSetCode == ResultSetCode.SUCCESS) {
            return new ResultSet<T>(true, resultSetCode.getCode(), resultSetCode.getMessage(), result, tips, 0);
        } else {
            return new ResultSet<T>(false, resultSetCode.getCode(), resultSetCode.getMessage(), result, tips, 0);
        }
    }
}
