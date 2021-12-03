package com.key.win.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.key.win.common.model.base.MybatisID;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


/**
* @author 作者 owen 
* @version 创建时间：2017年11月12日 上午22:57:51
* 类说明 日志实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_log")
@EqualsAndHashCode(callSuper=true)
public class SysLog extends MybatisID {

	private String username; //	用户名
	private String module;	//	归属模块
	private String params;	//	执行方法的参数值
	private String remark;  //  备注
	private Boolean flag;	//	是否执行成功
}
