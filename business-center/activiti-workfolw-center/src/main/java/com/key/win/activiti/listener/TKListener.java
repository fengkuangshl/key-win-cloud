package com.key.win.activiti.listener;

import com.key.win.activiti.feign.UserFeignClient;
import com.key.win.activiti.redis.RedisActivitiReceiver;
import com.key.win.common.auth.details.LoginAppUser;
import com.key.win.common.model.system.SysUser;
import com.key.win.common.util.JsonUtils;
import com.key.win.common.util.SpringUtils;
import com.key.win.common.util.StringUtil;
import com.key.win.common.web.Result;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class TKListener implements TaskListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void notify(DelegateTask delegateTask) {
        UserFeignClient userFeignClient = SpringUtils.getBean(UserFeignClient.class);
        RedisTemplate redisTemplate = SpringUtils.getBean(RedisTemplate.class);
        List<String> userNames = new ArrayList<>();
        String assignee = delegateTask.getAssignee();
        if (StringUtil.isNotBlank(assignee)) {
            userNames.add(assignee);
        }
        String taskDefinitionKey = delegateTask.getTaskDefinitionKey();
        String processInstanceId = delegateTask.getProcessInstanceId();
        String processDefinitionId = delegateTask.getProcessDefinitionId();
        Set<IdentityLink> candidates = delegateTask.getCandidates();
        if (!CollectionUtils.isEmpty(candidates)) {
            candidates.forEach(identityLink -> {
                if ("candidate".equals(identityLink.getType())) {
                    String groupId = identityLink.getGroupId();
                    if (StringUtil.isNotBlank(groupId)) {
                        Result<List<SysUser>> userByGroupId = userFeignClient.getUserByGroupCode(groupId);
                        if(userByGroupId.getCode() == 200){
                            List<SysUser> users = userByGroupId.getData();
                            users.forEach(sysUser -> {
                                userNames.add(sysUser.getUsername());
                            });
                        }
                    }
                    if (StringUtil.isNotBlank(identityLink.getUserId())) {
                        userNames.add(identityLink.getUserId());
                    }
                }
            });
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id",delegateTask.getId());
        map.put("userNames",userNames);
        map.put("taskDefinitionKey", taskDefinitionKey);
        map.put("processInstanceId", processInstanceId);
        map.put("processDefinitionId", processDefinitionId);
        String json = JsonUtils.toJsonNoException(map);
        logger.info("广播的任务信息：" + json);
        redisTemplate.convertAndSend(RedisActivitiReceiver.CHANNEL, json);
    }
}
