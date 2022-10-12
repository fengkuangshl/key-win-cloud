package com.key.win.param.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@TableName("sys_dic_data")
@EqualsAndHashCode(callSuper = true)
@ApiModel("字典数据")
public class SysDictData extends SysDictBaseData {

    /**
     * 是否默认（Y是 N否）
     */
    @ApiModelProperty("是否默认（Y是 N否）")
    private Boolean isDefault;

}