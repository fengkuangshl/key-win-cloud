package com.key.win.common.model.log;

import com.baomidou.mybatisplus.annotation.TableName;
import com.key.win.common.model.basic.MybatisID;
import lombok.*;


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
