package com.key.win.common.web;

import com.key.win.common.util.KeyWinConstantUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
public class PageResult<T> extends BaseResult {
    @ApiModelProperty("当前页数")
    @Builder.Default
    private int pageNo = 1;
    @ApiModelProperty("每页条数")
    @Builder.Default
    private int pageSize = 10;
    @ApiModelProperty("总条数")
    @Builder.Default
    private long count = 0;

    //总页数
    //private int					totalPage;

    @ApiModelProperty("分页数据列表")
    private List<T> data = new ArrayList<T>();

    @ApiModelProperty("总页数数")
    public long getTotalPage() {
        return (count + pageSize - 1) / pageSize;
    }

    public PageResult() {
    }

    public PageResult(Long count, List<T> data) {
        this(Integer.valueOf(1), Integer.valueOf(10), count, data);
    }

    public PageResult(Integer pageNo, Long count, List<T> data) {
        this(pageNo, Integer.valueOf(10), count, data);
    }

    public PageResult(Integer pageNo, Integer pageSize, Long count, List<T> data) {
        this(pageNo, pageSize, count, data, CodeEnum.SUCCESS.getCode(), KeyWinConstantUtils.RESULT_SUCCESS_DEFAULT_MESSAGE);

    }

    public PageResult(Integer pageNo, Integer pageSize, Long count, List<T> data, Integer code, String msg) {
        super(code, msg);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.count = count;
        this.data = data;
    }
}
