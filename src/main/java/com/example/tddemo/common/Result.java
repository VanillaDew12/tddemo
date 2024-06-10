package com.example.tddemo.common;

import lombok.Data;

@Data
public class Result {
    private static final String SUCCESS_CODE= "200";
    private static final String ERROR_CODE= "-1";


    private String code;
    private Object data;
    private String msg;

    public static Result success(){
        Result result = new Result();
        result.setCode("SUCCESS_CODE");
        return result;
    }
//    public static Result success(String code){
//        Result result = new Result();
//        result.setCode("SUCCESS_CODE");
//        result.setData(code);
//        return result;
//    }
    public static Result error(String msg){
        Result result = new Result();
        result.setCode("ERROR_CODE");
        result.setMsg(msg);
        return result;
    }
}
