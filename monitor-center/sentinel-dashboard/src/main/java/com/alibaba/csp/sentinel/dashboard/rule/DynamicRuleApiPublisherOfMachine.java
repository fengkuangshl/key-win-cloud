/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.RuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
@Component
public class DynamicRuleApiPublisherOfMachine {

    @Autowired
    private SentinelApiClient sentinelApiClient;
    @Autowired
    private AppManagement appManagement;

    public List<? extends RuleEntity> publish(String app, String type, List<? extends RuleEntity> rules) throws Exception {
        if (StringUtil.isBlank(app)) {
            return new ArrayList<>();
        }
        if (rules == null) {
            return new ArrayList<>();
        }
        Set<MachineInfo> set = appManagement.getDetailApp(app).getMachines();

        for (MachineInfo machine : set) {
            if (!machine.isHealthy()) {
                continue;
            }
            // TODO: parse the results
            sentinelApiClient.setRules(app, machine.getIp(), machine.getPort(), type, rules);
        }
        return rules;
    }

    public List<ParamFlowRuleEntity> publish(String app, List<ParamFlowRuleEntity> rules) {
        if (StringUtil.isBlank(app)) {
            return new ArrayList<>();
        }
        if (rules == null) {
            return new ArrayList<>();
        }
        Set<MachineInfo> set = appManagement.getDetailApp(app).getMachines();
        for (MachineInfo machine : set) {
            if (!machine.isHealthy()) {
                continue;
            }
            sentinelApiClient.setParamFlowRuleOfMachine(app, machine.getIp(), machine.getPort(), rules);
        }

        return rules;
    }

    public List<ApiDefinitionEntity> modifyApis(String app, List<ApiDefinitionEntity> apis){
        if (StringUtil.isBlank(app)) {
            return new ArrayList<>();
        }
        if (apis == null) {
            return new ArrayList<>();
        }
        Set<MachineInfo> set = appManagement.getDetailApp(app).getMachines();
        for (MachineInfo machine : set) {
            if (!machine.isHealthy()) {
                continue;
            }
            sentinelApiClient.modifyApis(app, machine.getIp(), machine.getPort(), apis);
        }

        return apis;
    }

    public List<GatewayFlowRuleEntity> modifyGatewayFlowRules(String app, List<GatewayFlowRuleEntity> rules){
        if (StringUtil.isBlank(app)) {
            return new ArrayList<>();
        }
        if (rules == null) {
            return new ArrayList<>();
        }
        Set<MachineInfo> set = appManagement.getDetailApp(app).getMachines();
        for (MachineInfo machine : set) {
            if (!machine.isHealthy()) {
                continue;
            }
            sentinelApiClient.modifyGatewayFlowRules(app, machine.getIp(), machine.getPort(), rules);
        }

        return rules;
    }
}
