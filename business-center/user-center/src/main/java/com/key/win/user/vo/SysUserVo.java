package com.key.win.user.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.key.win.common.model.system.SysRole;
import com.key.win.common.model.system.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@ApiModel("电子围栏VO")
@Data
public class SysUserVo extends SysUser {
    @ApiModelProperty("角色Id")
    @TableField(exist = false)
    private Long roleId;
    @ApiModelProperty("新密码")
    @TableField(exist = false)
    private String newPassword;
    @ApiModelProperty("组Id")
    @TableField(exist = false)
    private Long groupId;
    @ApiModelProperty("用户Id集合")
    @TableField(exist = false)
    private Set<Long> userIds;
    @ApiModelProperty("角色Id集合")
    @TableField(exist = false)
    private Set<Long> roleIds;
    @ApiModelProperty("组Id集合")
    @TableField(exist = false)
    private Set<Long> groupIds;
}
