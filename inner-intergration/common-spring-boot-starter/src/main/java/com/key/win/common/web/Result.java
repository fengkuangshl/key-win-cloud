package com.key.win.common.web;

import com.key.win.common.util.KeyWinConstantUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Result<T> extends BaseResult {
    @ApiModelProperty("返回实体")
    private T data;

    public Result(T data, Integer code, String msg) {
        super(code, msg);
        this.data = data;
    }

    public static <T> Result<T> succeed() {
        return succeed(KeyWinConstantUtils.RESULT_SUCCESS_DEFAULT_MESSAGE);
    }

    public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeedWith(model, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model) {
        return succeedWith(model, CodeEnum.SUCCESS.getCode(), KeyWinConstantUtils.RESULT_SUCCESS_DEFAULT_MESSAGE);
    }

    public static <T> Result<T> succeedWith(T data, Integer code, String msg) {
        return new Result<T>(data, code, msg);
    }

    public static <T> Result<T> failed() {
        return failed("KO");
    }

    public static <T> Result<T> failed(String msg) {
        return failed(CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(Integer code, String msg) {
        return failedWith(null, code, msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return failedWith(model, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failedWith(T data, Integer code, String msg) {
        return new Result<T>(data, code, msg);
    }

    public static <T> Result<T> result(boolean result) {
        return result ? Result.succeed(KeyWinConstantUtils.SUCCEED_INFO) : Result.failed(KeyWinConstantUtils.FAILED_INFO);
    }

}
