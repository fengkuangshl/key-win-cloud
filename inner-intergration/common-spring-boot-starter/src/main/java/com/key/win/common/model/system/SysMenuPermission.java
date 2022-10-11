package com.key.win.common.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@ApiModel("菜单权限实体")
@Data
@TableName("sys_menu_permission")
@EqualsAndHashCode(callSuper = true)
public class SysMenuPermission extends MybatisID {
    @ApiModelProperty("菜单Id")
    private Long menuId;
    @ApiModelProperty("权限Id")
    private Long permissionId;
    @ApiModelProperty("菜单权限Code")
    private String permissionCode;
    @ApiModelProperty("是否拥有此项功能")
    private Boolean checked;

    @ApiModelProperty("菜单名称")
    @TableField(exist = false)
    private String menuName;
    @ApiModelProperty("权限名称")
    @TableField(exist = false)
    private String permissionName;
    @ApiModelProperty("表格的属性")
    @TableField(exist = false)
    private String propertyName;
    @ApiModelProperty("是否需要删除")
    @TableField(exist = false)
    private boolean isDelete = Boolean.TRUE;
    @ApiModelProperty("菜单权限实体Id集合")
    @TableField(exist = false)
    private Set<Long> menuPermissionIds;

}
