package com.key.win.common.model.baseData;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 作者 gitgeek
 * @version 创建时间：2018-08-06 21:29
 * 类说明  菜单实体
 */
@Data
@TableName("sys_param")
@EqualsAndHashCode(callSuper = true)
public class SysParam extends SysBaseParam {

}
