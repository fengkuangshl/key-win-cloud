package com.key.win.es.controller;

import cn.hutool.core.util.StrUtil;
import com.key.win.common.web.CodeEnum;
import com.key.win.common.web.PageResult;
import com.key.win.es.dao.SqlSlowDao;
import com.key.win.es.entity.ServiceLogDocument;
import com.key.win.es.entity.SqlSlowDocument;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ELK收集mysql慢查询日志数据
 */
@RestController
public class SqlSlowController {
    private static final String ES_PARAM_QUERY = "query";

    @Autowired
    public SqlSlowDao sqlSlowDao;

    @ApiOperation(value = "系统日志查询列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")})
    @GetMapping(value = "/slowQueryLog")
    public PageResult<SqlSlowDocument> getPage(@RequestParam Map<String, Object> params) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        String searchKey = MapUtils.getString(params, "searchKey");
        String searchValue = MapUtils.getString(params, "searchValue");
        if (StrUtil.isNotEmpty(searchKey) && StrUtil.isNotEmpty(searchValue)) {
            // 模糊查询 相当于and
            builder.must(QueryBuilders.matchQuery(ES_PARAM_QUERY, searchValue));
        }

        Pageable pageable = PageRequest.of(MapUtils.getInteger(params, "page") - 1,
                MapUtils.getInteger(params, "limit")); //// Sort.Direction.DESC,"@timestamp"
        NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(builder).withPageable(pageable).build();
        Page<SqlSlowDocument> result = sqlSlowDao.search(build);

        return new PageResult<SqlSlowDocument>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"),result.getTotalElements(),result.getContent());
    }
}