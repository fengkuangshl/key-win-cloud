package com.key.win.client.error;

import com.key.win.common.web.Result;
import com.netflix.zuul.context.RequestContext;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * zuul自定义异常格式
 * (限流处理异常)
*/

@Controller
public class ZuulErrorController implements ErrorController {

    public static final String ERROR_PATH = "/error";

    /**
     * 错误最终会到这里来
     */
    @RequestMapping(ERROR_PATH)
    @ResponseBody
    public Object error() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        return Result.failed(throwable.getMessage()) ;
    }

    

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}