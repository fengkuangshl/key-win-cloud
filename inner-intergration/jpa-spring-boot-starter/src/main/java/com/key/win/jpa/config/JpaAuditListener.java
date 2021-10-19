package com.key.win.jpa.config;

import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.model.SysUser;
import com.key.win.common.util.SysUserUtil;
import com.key.win.jpa.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.springframework.security.config.Elements.ANONYMOUS;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class JpaAuditListener {



    private static final Logger logger = LoggerFactory.getLogger(JpaAuditListener.class);

    /**
     * 新增数据时，填充创建人和创建时间
     */
    @PrePersist
    public void prePersist(Object object) {
        if (object instanceof BaseModel) {
            logger.info("object is BaseModel");
            BaseModel baseModel = (BaseModel) object;
            baseModel.setCreateUserName(this.getUserName());
        }


    }

    /**
     * 更新数据时，填充更新人和更新时间
     */
    @PreUpdate
    public void preUpdate(Object object) {
        if (object instanceof BaseModel) {
            logger.info("object is BaseModel");
            BaseModel baseModel = (BaseModel) object;
            baseModel.setUpdateUserName(this.getUserName());
        }
    }

    /**
     * 新增数据之后的操作
     */
    @PostPersist
    public void postPersist(Object object)
            throws IllegalArgumentException, IllegalAccessException {
        logger.info("data save after .....");
    }

    /**
     * 更新数据之后的操作
     */
    @PostUpdate
    public void postUpdate(Object object)
            throws IllegalArgumentException, IllegalAccessException {
        logger.info("data update after .....");
    }

    /**
     * @return
     */
    protected String getUserName() {
        LoginAppUser loginAppUser = SysUserUtil.getLoginAppUser();
        String userName = "";
        if (loginAppUser != null) {
            userName = loginAppUser.getUsername();
        } else {
            userName = ANONYMOUS;
        }
        logger.info("system userName :" + userName);
        return userName;
    }

}
