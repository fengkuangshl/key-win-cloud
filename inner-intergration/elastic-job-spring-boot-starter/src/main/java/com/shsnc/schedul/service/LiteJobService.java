package com.shsnc.schedul.service;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.config.LiteJobConfigurationGsonFactory;
import com.dangdang.ddframe.job.lite.lifecycle.internal.operate.JobOperateAPIImpl;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.google.common.base.Optional;
import com.shsnc.schedul.vo.LiteJobVO;
import com.shsnc.zookeeper.ZookeeperRegistryRetryForeverCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Slf4j
public class LiteJobService {

    final ZookeeperRegistryRetryForeverCenter registryCenter;

    final JobOperateAPIImpl jobOperateAPI;

    @Autowired
    private ApplicationContext applicationContext;

    public static final String PREFIX = "LITEJOB-";

    private static final String CONFIG_NODE = "config";

    public LiteJobService(ZookeeperRegistryRetryForeverCenter registryCenter) {
        this.registryCenter = registryCenter;
        jobOperateAPI = new JobOperateAPIImpl(registryCenter);
    }

    /**
     * ??????????????????
     * @param jobName
     */
    public void triggerJob(String jobName){
        jobOperateAPI.trigger(Optional.of(jobName),Optional.fromNullable(null));
    }

    /**
     * ????????????
     * @param jobName
     */
    public void disableJob(String jobName){
        jobOperateAPI.disable(Optional.of(jobName),Optional.fromNullable(null));
    }

    /**
     * ????????????
     * @param jobName
     */
    public void enableJob(String jobName){
        jobOperateAPI.enable(Optional.of(jobName),Optional.fromNullable(null));
    }

    /**
     * ????????????
     *  ??????zk??????????????? ???????????????????????? zk????????????????????????????????????
     * @param jobName
     */
    public void shutdownJob(String jobName){
        jobOperateAPI.shutdown(Optional.of(jobName),Optional.fromNullable(null));
    }

    /**
     * ?????? jobName ?????? ????????????
     * @param jobName
     * @return
     */
    public boolean existJob(String jobName){
        return registryCenter.isExisted("/" +jobName);
    }

    /**
     * ??????jobName ?????? ????????????
     * @param jobName
     */
    public void deleteJob(String jobName)  {
        CuratorFramework client = registryCenter.getClient();
        boolean existed = existJob(jobName);
        try {
            if (existed) {
                client.delete().deletingChildrenIfNeeded().forPath("/" + jobName);
            }
        }catch (Exception e){}

        // TODO: 2020/9/2 ??????IOC?????????Bean
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        try {
            Object bean = applicationContext.getBean(PREFIX + jobName);
            log.info("???"+ jobName +"??? : {}" , JSON.toJSONString(bean));
            if (Objects.nonNull(bean)){
                defaultListableBeanFactory.removeBeanDefinition(PREFIX + jobName);
            }
        } catch (Exception e) {
            log.error("ERROR NO BEAN,DELETE BEAN " + PREFIX + jobName);
        }
    }

    /**
     * ??????????????????
     *  ???????????????watch??????
     */
    public void updateJobConfig(LiteJobVO liteJobVO){
        log.info("???"  + liteJobVO.getJobName() + "???  "  + "   UPDATEJOBCONFIG BEGIN   ");
        try {
            LiteJobConfiguration jobConfig = createLiteJobConfiguration(liteJobVO);
            log.info("jobConfig : {}",LiteJobConfigurationGsonFactory.toJson(jobConfig));
            registryCenter.persist( "/"+ liteJobVO.getJobName() +"/"+ CONFIG_NODE,LiteJobConfigurationGsonFactory.toJson(jobConfig));
        }catch (Exception e){}
        log.info("???"  + liteJobVO.getJobName() + "???  "  + "   UPDATEJOBCONFIG END     ");
    }

    /**
     * ?????? jobName ?????? LiteJobVO
     *
     * @param jobName
     * @return
     */
    public LiteJobVO selectJobConfig(String jobName){
        log.info("???"  + jobName + "???  "  + "   SELECTJOBCONFIG BEGIN   ");
        LiteJobVO liteJobVO = null;
        try {
            String liteJobConfigJson = registryCenter.get("/" + jobName + "/" + CONFIG_NODE);
            log.info("???"  + jobName + "???  "  + "   liteJobConfigJson:{}  ",liteJobConfigJson);
            LiteJobConfiguration jobConfig  = LiteJobConfigurationGsonFactory.fromJson(liteJobConfigJson);

            liteJobVO = createLiteJobVO(jobConfig);
        }catch (Exception e){}
        log.info("???"  + jobName + "???  "  + "   SELECTJOBCONFIG END     ");
        return liteJobVO;
    }


    /**
     * ????????????
     *
     * @param liteJobVO
     */
    public void addJob(LiteJobVO liteJobVO) {
        try {
            log.info("???"  + liteJobVO.getJobName() + "???  " + liteJobVO.getJobClass() + "   INIT BEGIN   ");

            LiteJobConfiguration jobConfig = createLiteJobConfiguration(liteJobVO);

            List<BeanDefinition> elasticJobListeners = getTargetElasticJobListeners(liteJobVO);

            // TODO: 2020/9/1 ??????SpringJobScheduler????????????????????????
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            if ("ScriptJob".equals(liteJobVO.getJobType())) {
                factory.addConstructorArgValue(null);
            } else {
                BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(liteJobVO.getJobClass());
                factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
            }
            factory.addConstructorArgValue(registryCenter);
            factory.addConstructorArgValue(jobConfig);

            // TODO: 2020/9/1 ?????????????????????????????????????????????
            if (StringUtils.hasText(liteJobVO.getEventTraceRdbDataSource())) {
                BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                rdbFactory.addConstructorArgReference(liteJobVO.getEventTraceRdbDataSource());
                factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
            }
            factory.addConstructorArgValue(elasticJobListeners);
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            defaultListableBeanFactory.registerBeanDefinition(PREFIX + liteJobVO.getJobName(), factory.getBeanDefinition());
            SpringJobScheduler springJobScheduler = (SpringJobScheduler) applicationContext.getBean( PREFIX + liteJobVO.getJobName());
            // TODO: 2020/9/1  ???????????????
            springJobScheduler.init();
            log.info("??? "  + liteJobVO.getJobName() + "???   " + liteJobVO.getJobClass() + "   INIT END   ");
        }catch (Exception e){
            log.info( "ERROR NO ???addJob???:{} , Exception : {} ",liteJobVO.getJobName(),e );
        }
    }

    /**
     * ????????????  liteJobVO -??? LiteJobConfiguration
     * @param liteJobVO
     * @return
     */
    public LiteJobConfiguration createLiteJobConfiguration(LiteJobVO liteJobVO){
        // TODO: 2020/9/1  ??????????????????
        JobCoreConfiguration coreConfig =
                JobCoreConfiguration.newBuilder(liteJobVO.getJobName(), liteJobVO.getCron(), liteJobVO.getShardingTotalCount())
                        .shardingItemParameters(liteJobVO.getShardingItemParameters())
                        .description(liteJobVO.getDescription())
                        .failover(liteJobVO.isFailover())
                        .jobParameter(liteJobVO.getJobParameter())
                        .misfire(liteJobVO.isMisfire())
                        .build();

        // TODO: 2020/9/1 ?????????????????????????????????
        LiteJobConfiguration jobConfig = null;
        JobTypeConfiguration typeConfig = null;

        // TODO: 2020/9/1 ????????????????????????????????????  ???????????????SIMPLE???DATAFLOW???SCRIPT???
        String jobType = liteJobVO.getJobType();
        switch (jobType) {
            case "SIMPLE" :
                typeConfig = new SimpleJobConfiguration(coreConfig, liteJobVO.getJobClass());
                break;
            case "DATAFLOW" :
                typeConfig = new DataflowJobConfiguration(coreConfig, liteJobVO.getJobClass(), liteJobVO.isStreamingProcess());
                break;
            case "SCRIPT" :
                typeConfig = new ScriptJobConfiguration(coreConfig, liteJobVO.getScriptCommandLine());
                break;
        }

        // TODO: 2020/9/1  Lite????????????
        jobConfig = LiteJobConfiguration.newBuilder(typeConfig)
                .overwrite(liteJobVO.isOverwrite())
                .disabled(liteJobVO.isDisabled())
                .monitorPort(liteJobVO.getMonitorPort())
                .monitorExecution(liteJobVO.isMonitorExecution())
                .maxTimeDiffSeconds(liteJobVO.getMaxTimeDiffSeconds())
                .jobShardingStrategyClass(liteJobVO.getJobShardingStrategyClass())
                .reconcileIntervalMinutes(liteJobVO.getReconcileIntervalMinutes())
                .build();

        return jobConfig;
    }


    /**
     *  ????????????   LiteJobConfiguration -???LiteJobVO
     * @param jobConfig
     * @return
     */
    public LiteJobVO createLiteJobVO(LiteJobConfiguration jobConfig){
        // TODO: 2020/9/4 ?????? builder ????????????
        LiteJobVO liteJobVO = LiteJobVO.builder()
                .monitorExecution(jobConfig.isMonitorExecution())
                .maxTimeDiffSeconds(jobConfig.getMaxTimeDiffSeconds())
                .monitorPort(jobConfig.getMonitorPort())
                .jobShardingStrategyClass(jobConfig.getJobShardingStrategyClass())
                .reconcileIntervalMinutes(jobConfig.getReconcileIntervalMinutes())
                .disabled(jobConfig.isDisabled())
                .overwrite(jobConfig.isOverwrite())
                .jobType(jobConfig.getTypeConfig().getJobType().name())
                .jobClass(jobConfig.getTypeConfig().getJobClass())
                .jobName(jobConfig.getJobName())
                .cron(jobConfig.getTypeConfig().getCoreConfig().getCron())
                .shardingTotalCount(jobConfig.getTypeConfig().getCoreConfig().getShardingTotalCount())
                .shardingItemParameters(jobConfig.getTypeConfig().getCoreConfig().getShardingItemParameters())
                .jobParameter(jobConfig.getTypeConfig().getCoreConfig().getJobParameter())
                .failover(jobConfig.getTypeConfig().getCoreConfig().isFailover())
                .misfire(jobConfig.getTypeConfig().getCoreConfig().isMisfire())
                .description(jobConfig.getTypeConfig().getCoreConfig().getDescription())
                .jobExceptionHandler(jobConfig.getTypeConfig().getCoreConfig().getJobProperties().get(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER))
                .executorServiceHandler(jobConfig.getTypeConfig().getCoreConfig().getJobProperties().get(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER))
                .build();
        return liteJobVO;
    }




    // TODO: 2020/8/30  ??????ElasticJob???????????????
    private List<BeanDefinition> getTargetElasticJobListeners(LiteJobVO liteJobVO) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
        String listeners = liteJobVO.getListener();
        if (StringUtils.hasText(listeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            result.add(factory.getBeanDefinition());
        }

        String distributedListeners = liteJobVO.getDistributedListener();
        long startedTimeoutMilliseconds = liteJobVO.getStartedTimeoutMilliseconds();
        long completedTimeoutMilliseconds = liteJobVO.getCompletedTimeoutMilliseconds();

        if (StringUtils.hasText(distributedListeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListeners);
            factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
            factory.addConstructorArgValue(startedTimeoutMilliseconds);
            factory.addConstructorArgValue(completedTimeoutMilliseconds);
            result.add(factory.getBeanDefinition());
        }
        return result;
    }

    /**
     * ??????????????????,??????????????????????????????zk???????????????????????????????????????????????????????????????
     */
    public void monitorJobRegister() {
        CuratorFramework client = registryCenter.getClient();
        @SuppressWarnings("resource")
        PathChildrenCache childrenCache = new PathChildrenCache(client, "/", true);

        PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED:
                        boolean existed = registryCenter.isExisted(data.getPath() + "/" + CONFIG_NODE);
                        log.info("  ???????????? : {} , config is existed : {}" , data.getPath(),existed );
                        if (existed){
                            String config = new String(client.getData().forPath(data.getPath() + "/" + CONFIG_NODE));
                            log.info("config : {}" , config);
                            LiteJobVO liteJobVO = JSON.parseObject(config,LiteJobVO.class);
                            Object bean = null;
                            try {
                                bean = applicationContext.getBean(PREFIX + liteJobVO.getJobName());
                            }catch (BeansException e){
                                log.error("ERROR NO BEAN,CREATE BEAN " + PREFIX + liteJobVO.getJobName());
                            }
                            // TODO: 2020/9/1 ????????????
                            if (Objects.isNull(bean)){
                                addJob(liteJobVO);
                            }
                        }else {
                            // TODO: 2020/8/31 ??????????????????
                            if (registryCenter.isExisted(data.getPath())){
                                shutdownJob(data.getPath().replace("/",""));
                                deleteJob(data.getPath().replace("/",""));
                            }
                        }
                        break;
                    case CHILD_REMOVED:
                        // TODO: 2020/9/2 ??????IOC?????????Bean
                        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
                        try {
                            Object bean = applicationContext.getBean(PREFIX + data.getPath().replace("/",""));
                            log.info("???"+ data.getPath().replace("/","") +"??? : {}" , JSON.toJSONString(bean));
                            if (Objects.nonNull(bean)){
                                defaultListableBeanFactory.removeBeanDefinition(PREFIX + data.getPath().replace("/",""));
                            }
                        } catch (Exception e) {
                            log.error("ERROR NO BEAN,DELETE BEAN " + PREFIX + data.getPath().replace("/",""));
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        try {
            // https://blog.csdn.net/u010402202/article/details/79581575
            childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
