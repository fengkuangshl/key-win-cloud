package com.key.win.datalog.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.key.win.common.util.StringUtil;
import com.key.win.datalog.annotation.DataLog;
import com.key.win.datalog.handle.BaseDataLog;
import com.key.win.datalog.handle.DataChange;
import lombok.AllArgsConstructor;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * DataLog切面
 * 日志记录的切面，进入目标方法时（被@DataLog标注的方法时）,被拦截，先进行入此类
 * </p>
 *
 * @author Tophua
 * @since 2020/7/15
 */
@Aspect
@Order(99)
@Component
@AllArgsConstructor
public class DataLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLogAspect.class);

    //日志记录基础类
    private final BaseDataLog baseDataLog;

    /**
     * <p>
     * 初始化
     * 主要用来
     * 设置排除某张表、某些字段
     * </p>
     *
     * @return void
     * @author Tophua
     * @since 2020/10/30
     */
    @PostConstruct
    public void init() {
        baseDataLog.setting();
    }

    /**
     * <p>
     * 切面前执行
     * </p>
     *
     * @param dataLog dataLog
     * @return void
     * @author Tophua
     * @since 2020/7/15
     */
    @Before("@annotation(dataLog)")
    public void before(JoinPoint joinPoint, DataLog dataLog) {
        // 使用 ThreadLocal 记录一次操作
        BaseDataLog.DATA_CHANGES.set(new LinkedList<>());//数据变化 多张表-多条数据
        BaseDataLog.JOIN_POINT.set(joinPoint);//当前切点的信息
        BaseDataLog.DATA_LOG.set(dataLog);//当前方法注解的信息@DataLog
        if (baseDataLog.isIgnore(dataLog)) {//是否需要此次操作
            BaseDataLog.DATA_CHANGES.set(null);
        }
    }

    /**
     * <p>
     * 切面后执行
     * </p>
     *
     * @param dataLog dataLog
     * @return void
     * @author Tophua
     * @since 2020/7/15
     */
    @AfterReturning("@annotation(dataLog)")
    public void after(DataLog dataLog) {
        List<DataChange> list = BaseDataLog.DATA_CHANGES.get();//获取本次请求的变更信息
        if (CollUtil.isEmpty(list)) {
            return;
        }
        list.forEach(change -> {
            List<?> oldData = change.getOldData();
            if (CollUtil.isEmpty(oldData)) {
                return;
            }
            List<String> ids = oldData.stream()
                    .map(o -> ReflectUtil.invoke(o, "getId").toString())
                    .filter(ObjectUtil::isNotNull)
                    .map(String::toString)
                    .collect(Collectors.toList());
            SqlSession sqlSession = change.getSqlSessionFactory().openSession();
            try {
                Map<String, Object> map = new HashMap<>(1);
                map.put(Constants.WRAPPER, Wrappers.query().in("id", ids));
                List<?> newData = sqlSession.selectList(change.getSqlStatement(), map);
                change.setNewData(Optional.ofNullable(newData).orElse(new ArrayList<>()));
            } finally {
                SqlSessionUtils.closeSqlSession(sqlSession, change.getSqlSessionFactory());
            }
            LOGGER.info("oldData:" + JSONUtil.toJsonStr(change.getOldData()));
            LOGGER.info("newData:" + JSONUtil.toJsonStr(change.getNewData()));
        });
        // 对比调模块
        this.compareAndTransfer(list);
    }

    /**
     * <p>
     * 对比保存
     * </p>
     *
     * @param list list
     * @return void
     * @author Tophua
     * @since 2020/7/15
     */
    public void compareAndTransfer(List<DataChange> list) {
        StringBuilder sb = new StringBuilder();
        StringBuilder rsb = new StringBuilder();
        StringBuilder newFkIds = new StringBuilder();
        list.forEach(change -> {
            List<?> oldData = change.getOldData();
            List<?> newData = change.getNewData();
            // 更新前后数据量不对必定是删除（逻辑删除）不做处理
            if (newData == null) {
                return;
            }
            if (oldData == null) {
                return;
            }
            if (oldData.size() != newData.size()) {
                return;
            }
            // 按id排序
            oldData.sort(Comparator.comparing(d -> ReflectUtil.invoke(d, "getId").toString()));
            newData.sort(Comparator.comparing(d -> ReflectUtil.invoke(d, "getId").toString()));
            for (int i = 0; i < oldData.size(); i++) {
                final int[] finalI = {0};
                baseDataLog.sameClazzDiff(oldData.get(i), newData.get(i)).forEach(r -> {
                    String oldV = r.getOldValue() == null ? "无" : r.getOldValue().toString();
                    String newV = r.getNewValue() == null ? "无" : r.getNewValue().toString();
                    if (ObjectUtil.equal(oldV.trim(), newV.trim())) {
                        return;
                    }
                    if (finalI[0] == 0) {
                        sb.append(StrUtil.LF);
                        sb.append(StrUtil.format("修改表：【{}】", change.getTableName()));
                        sb.append(StrUtil.format("id：【{}】", r.getId()));
                        newFkIds.append(r.getId()+",");
                    }
                    sb.append(StrUtil.LF);
                    rsb.append(StrUtil.LF);
                    sb.append(StrUtil.format("把字段[{}]从[{}]改为[{}]",
                            r.getFieldName(), r.getOldValue(), r.getNewValue()));
                    rsb.append(StrUtil.indexedFormat(baseDataLog.getLogFormat(),
                            r.getId(), r.getFieldName(), r.getFieldComment(),
                            oldV, newV));
                    finalI[0]++;
                });
            }
        });
        if (sb.length() > 0) {
            sb.deleteCharAt(0);
            rsb.deleteCharAt(0);
            String fkId = BaseDataLog.FK_ID.get();
            if (StringUtil.isEmpty(fkId)) {
                BaseDataLog.FK_ID.set(newFkIds.substring(0,newFkIds.length()-1));
                LOGGER.info("fkId为空，将对fkId赋值: " + BaseDataLog.FK_ID.get());
            } else {
                LOGGER.info("fkId不为空，不对fkId赋值");
            }
        }

        // 存库
        LOGGER.info(sb.toString());
        LOGGER.info(rsb.toString());

        BaseDataLog.DATA_CHANGES.set(list);
        BaseDataLog.LOG_STR.set(sb.toString());
        baseDataLog.transfer();
    }

}
