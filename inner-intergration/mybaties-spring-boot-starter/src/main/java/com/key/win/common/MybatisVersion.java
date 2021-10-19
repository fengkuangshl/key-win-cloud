package com.key.win.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;

@Data
public class MybatisVersion implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Version
    private Integer           version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
