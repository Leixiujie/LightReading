package com.cqupt.readingcloud.common.result;

import java.io.Serializable;

/**
 * 操作结果集封装
 * @author zealon
 */
public class Result<T>  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int code;
    private String msg;
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result() {

    }

    /**
     * 构建消息内容
     * @param msg
     * @return
     */
    public Result buildMessage(String msg){
        this.setMsg(msg);
        return this;
    }

    /**
     * 构建消息data的值，key默认为data
     * @param obj data值
     * @return
     */
    public Result buildData(T obj){
        this.setData(obj);
        return this;

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
