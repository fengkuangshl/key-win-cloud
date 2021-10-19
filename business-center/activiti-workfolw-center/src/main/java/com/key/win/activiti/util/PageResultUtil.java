package com.key.win.activiti.util;

import com.key.win.common.web.CodeEnum;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;

import java.util.List;

public class PageResultUtil {
    //设置分页返回参数
    public static PageResult constructPageResult(PageRequest pageParam, long total, List records) {
        PageResult pageResult = new PageResult();
        pageResult.setCount(total);
        pageResult.setPageNo(pageParam.getPageNo());
        pageResult.setPageSize(pageParam.getPageSize());
        pageResult.setData(records);
        pageResult.setCode(CodeEnum.SUCCESS.getCode());
        return pageResult;
    }
}
