package com.key.win.common.model.basic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MybatisID extends MybatisCommonFleid {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId(type=IdType.ASSIGN_ID)
    private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
