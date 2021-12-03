package com.key.win.datalog.handle;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.key.win.common.model.basic.MybatisID;
import com.key.win.datalog.annotation.DataLog;
import com.key.win.datalog.annotation.IgnoreDataLog;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据日志基类，模块使用时继承此类
 * </p>
 *
 * @author Tophua
 * @since 2020/8/11
 */
public abstract class BaseDataLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDataLog.class);
    /**
     * 注解
     */
    public static final ThreadLocal<DataLog> DATA_LOG = new ThreadLocal<>();
    /**
     * 切点
     */
    public static final ThreadLocal<JoinPoint> JOIN_POINT = new ThreadLocal<>();
    /**
     * 数据变化 多张表-多条数据
     */
    public static final ThreadLocal<List<DataChange>> DATA_CHANGES = new ThreadLocal<>();
    /**
     * 全部变化对比结果
     */
    public static final ThreadLocal<List<CompareResult>> COMPARE_RESULTS = ThreadLocal.withInitial(ArrayList::new);
    /**
     * 全部变化记录 默认：将[{}]由{}修改为{}
     */
    public static final ThreadLocal<String> LOG_STR = new ThreadLocal<>();

    public static final ThreadLocal<String> FK_ID = new ThreadLocal<>();

    /**
     * 日志格式
     * 0：数据id
     * 1：字段名
     * 2：字段注释
     * 3：字段旧值
     * 4：字段新值
     *
     * 如：将[{2}]由{3}修改为{4} ==》将[名字]由张三修改为王五
     */
    @Setter
    @Getter
    private String logFormat = "将[{2}]由{3}修改为{4}";


    /**
     * 排除表名
     * 排除表不进行日志记录
     */
    @Setter
    @Getter
    public static List<String> excludeTableNames = new ArrayList<>();

    /**
     * 排除字段名
     * 排除字段不进行对比
     */
    @Setter
    @Getter
    private List<String> excludeFieldNames = new ArrayList<>();

    /**
     * 用于SpEL表达式解析.
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();;
    /**
     * 用于获取方法参数定义名字.
     */
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * <p>
     * 调用
     * </p>
     *
     * @return void
     * @author Tophua
     * @since 2020/8/11
     */
    public void transfer() {
        DataLogVo data = new DataLogVo();
        data.setDataChanges(DATA_CHANGES.get());
        data.setCompareResults(COMPARE_RESULTS.get());
        data.setLogStr(LOG_STR.get());
        data.setFkId(FK_ID.get());
        this.change(DATA_LOG.get(), data);

        DATA_CHANGES.remove();
        COMPARE_RESULTS.remove();
        LOG_STR.remove();
        DATA_LOG.remove();
        JOIN_POINT.remove();
        FK_ID.remove();
    }

    /**
     * <p>
     * 是否忽略
     * </p>
     *
     * @param dataLog dataLog
     * @return boolean
     * @author Tophua
     * @since 2020/12/22
     */
    public boolean isIgnore(DataLog dataLog) {
        return false;
    }


    /**
     * <p>
     * 增加排除表名
     * </p>
     *
     * @param tableName tableName
     * @return cn.rc100.common.data.log.BaseDataLog
     * @author Tophua
     * @since 2020/11/2
     */
    public BaseDataLog addExcludeTableName(String tableName) {
        excludeTableNames.add(tableName);
        return this;
    }

    /**
     * <p>
     * 增加排除字段名
     * </p>
     *
     * @param fieldName fieldName
     * @return cn.rc100.common.data.log.BaseDataLog
     * @author Tophua
     * @since 2020/9/7
     */
    public BaseDataLog addExcludeFieldName(String fieldName) {
        this.excludeFieldNames.add(fieldName);
        return this;
    }


    /**
     * <p>
     * 设置
     * 可在方法内设置 数据字典是否翻译 返回记录文字描述格式
     * </p>
     *
     * @return void
     * @author Tophua
     * @since 2020/8/13
     */
    public abstract void setting();

    /**
     * <p>
     * 变化
     * </p>
     *
     * @param dataLog 注解
     * @param data 日志数据
     * @return void
     * @author Tophua
     * @since 2020/8/11
     */
    public abstract void change(DataLog dataLog, DataLogVo data);

    /**
     * <p>
     * 相同类对比
     * </p>
     *
     * @param obj1 obj1
     * @param obj2 obj2
     * @return java.util.List<com.lith.datalog.handle.CompareResult>
     * @author Tophua
     * @since 2020/7/15
     */
    public List<CompareResult> sameClazzDiff(Object obj1, Object obj2) {
        MybatisID idEntity = (MybatisID) obj1;
        List<CompareResult> results = new ArrayList<>();
        Field[] obj1Fields = obj1.getClass().getDeclaredFields();
        Field[] obj2Fields = obj2.getClass().getDeclaredFields();
        String id = "";
        for (int i = 0; i < obj1Fields.length; i++) {
            obj1Fields[i].setAccessible(true);
            obj2Fields[i].setAccessible(true);
            Field field = obj1Fields[i];
            if (this.excludeFieldNames.contains(field.getName()) || field.isAnnotationPresent(IgnoreDataLog.class)) {
                continue;
            }
            try {
                Object value1 = obj1Fields[i].get(obj1);
                Object value2 = obj2Fields[i].get(obj2);
                if (value1 == null || StrUtil.isBlank(value1.toString())) {
                    value1 = null;
                }
                if (value2 == null || StrUtil.isBlank(value2.toString())) {
                    value2 = null;
                }
                if (!ObjectUtil.equal(value1, value2)) {
                    if (value1 != null && value2 != null && ObjectUtil.equal(value1.toString().trim(), value2.toString().trim())) {
                        continue;
                    }
                    CompareResult r = new CompareResult();
                    r.setId(idEntity.getId());
                    r.setFieldName(field.getName());
                    // 获取注释
                    r.setFieldComment(field.getName());
//                    ApiModelProperty property = field.getAnnotation(ApiModelProperty.class);
//                    if (property != null && StrUtil.isNotBlank(property.value())) {
//                        r.setFieldComment(property.value());
//                    }
                    r.setOldValue(value1);
                    r.setNewValue(value2);
                    results.add(r);
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("相同类对比出错："+e.getMessage(),e);
            }
        }
        COMPARE_RESULTS.get().addAll(results);
        return results;
    }

    /**
     * 根据SpEL表达式和切面返回方法参数值
     *
     * @param spEl SpEL表达式
     * @param clazz 返回数据类型
     * @return T
     * @author Tophua
     * @date 2019/9/25
     */
    protected <T> T getValueBySpEl(String spEl, Class<T> clazz) {
        if (!spEl.contains("#")) {
            //注解的值非SPEL表达式，直接解析就好
            return JSONUtil.toBean(spEl, clazz);
        }
        JoinPoint joinPoint = JOIN_POINT.get();
        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 使用spring的DefaultParameterNameDiscoverer获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        if (paramNames == null || paramNames.length == 0) {
            return null;
        }
        // 解析过后的Spring表达式对象
        Expression expression = parser.parseExpression(spEl);
        // spring的表达式上下文对象
        EvaluationContext context = new StandardEvaluationContext(joinPoint);
        // 通过joinPoint获取被注解方法的形参
        Object[] args = joinPoint.getArgs();
        // 给上下文赋值
        for(int i = 0 ; i < args.length ; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        // 表达式从上下文中计算出实际参数值
        /*如:
            @annotation(id="#student.name")
             method(Student student)
             那么就可以解析出方法形参的某属性值，return “xiaoming”;
          */
        return expression.getValue(context, clazz);
    }
}
