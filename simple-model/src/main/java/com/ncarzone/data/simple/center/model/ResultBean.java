package com.ncarzone.data.simple.center.model;


/**
 * 操作结果
 *
 */
public class ResultBean<T> {

    private boolean success;

    private String  message;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static <T> ResultBean<T> getSuccessBean(T data) {
        ResultBean<T> bean = new ResultBean<T>();
        bean.setSuccess(true);
        bean.setMessage("操作成功");
        bean.setData(data);
        return bean;
    }
    
    public static <T> ResultBean<T> getSuccessBean(T data, String message) {
        ResultBean<T> bean = new ResultBean<T>();
        bean.setSuccess(true);
        bean.setMessage(message);
        bean.setData(data);
        return bean;
    }

    public static <T> ResultBean<T> getSuccessBean() {
        return getSuccessBean(null);
    }

    public static <T> ResultBean<T> getErrorBean(String message) {
        ResultBean<T> bean = new ResultBean<T>();
        bean.setSuccess(false);
        bean.setMessage(message);
        return bean;
    }

    public static <T> ResultBean<T> getErrorBean(String message, T data) {
        ResultBean<T> bean = new ResultBean<T>();
        bean.setSuccess(false);
        bean.setMessage(message);
        bean.setData(data);
        return bean;
    }

}
