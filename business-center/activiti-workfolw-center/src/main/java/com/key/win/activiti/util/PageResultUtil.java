package com.key.win.activiti.util;

import com.key.win.common.web.CodeEnum;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import org.apache.commons.collections4.MapUtils;

import java.util.List;

public class PageResultUtil {
    //设置分页返回参数
    public static PageResult constructPageResult(PageRequest pageParam, long total, List records) {
        return new PageResult(pageParam.getPageNo(),pageParam.getPageSize(),total,records);
    }
}
