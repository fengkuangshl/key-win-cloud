package com.key.win.datalog.handle;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.key.win.datalog.annotation.IgnoreDataLog;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.*;

/**
 * <p>
 * 数据更新拦截器
 * </p>
 *
 * @author Tophua
 * @since 2020/5/11
 */
@Slf4j
@AllArgsConstructor
@Intercepts({
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class})
})
public class DataUpdateInterceptor extends AbstractSqlParserHandler implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLogHandle.class);
    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {
        // 判断是否需要记录日志
        if (BaseDataLog.DATA_CHANGES.get() == null) {
            return invocation.proceed();
        }
        Statement statement;
        Object firstArg = invocation.getArgs()[0];
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }
        MetaObject stmtMetaObj = SystemMetaObject.forObject(statement);
        try {
            statement = (Statement) stmtMetaObj.getValue("stmt.statement");
        } catch (Exception e) {
            // do nothing
        }
        if (stmtMetaObj.hasGetter("delegate")) {
            //Hikari
            try {
                statement = (Statement) stmtMetaObj.getValue("delegate");
            } catch (Exception ignored) {

            }
        }

        String originalSql = statement.toString();
        originalSql = originalSql.replaceAll("[\\s]+", StringPool.SPACE);
        int index = indexOfSqlStart(originalSql);
        if (index > 0) {
            originalSql = originalSql.substring(index);
        }
        LOGGER.info("执行SQL：" + originalSql);

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        // 获取执行Sql
        String sql = originalSql.replace("where", "WHERE");
        // 插入
        if (SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
        }
        // 更新
        if (SqlCommandType.UPDATE.equals(mappedStatement.getSqlCommandType())) {
            // 使用mybatis-plus 工具解析sql获取表名
            Collection<String> tables = new TableNameParser(sql).tables();
            if (CollectionUtils.isEmpty(tables)) {
                return invocation.proceed();
            }
            String tableName = tables.iterator().next();
            // 排除表名判断
            if (BaseDataLog.excludeTableNames.contains(tableName)) {
                return invocation.proceed();
            }
            TableInfo tableInfo = null;
            for (TableInfo t: TableInfoHelper.getTableInfos()) {
                if(t.getTableName().equals(tableName)){
                    tableInfo = t;
                    break;
                }
            }
//            // 使用mybatis-plus 工具根据表名找出对应的实体类
//            TableInfo tableInfo = Optional.ofNullable(TableInfoHelper.getTableInfo(Class.forName(tableName)))
//                    .orElse(new TableInfo());
            Class<?> entityType = tableInfo.getEntityType();

            if (entityType == null || entityType.isAnnotationPresent(IgnoreDataLog.class)) {
                return invocation.proceed();
            }
            DataChange change = new DataChange();
            change.setTableName(tableName);
            change.setEntityType(entityType);
            // 设置sql用于执行完后查询新数据
            String selectSql = "AND " + sql.substring(sql.lastIndexOf("WHERE") + 5);
            // 同表对同条数据操作多次只进行一次对比
            if (BaseDataLog.DATA_CHANGES.get().stream().anyMatch(c -> tableName.equals(c.getTableName())
                    && selectSql.equals(c.getWhereSql()))) {
                return invocation.proceed();
            }
            change.setWhereSql(selectSql);
            Map<String, Object> map = new HashMap<>(1);
            map.put(Constants.WRAPPER, Wrappers.query().eq("1", 1).last(selectSql));
            // 查询更新前数据
            SqlSessionFactory sqlSessionFactory = GlobalConfigUtils.currentSessionFactory(entityType);
            change.setSqlSessionFactory(sqlSessionFactory);
            change.setSqlStatement(tableInfo.getSqlStatement(SqlMethod.SELECT_LIST.getMethod()));
            SqlSession sqlSession = sqlSessionFactory.openSession();
            try {
                List<?> oldData = sqlSession.selectList(change.getSqlStatement(), map);
                change.setOldData(Optional.ofNullable(oldData).orElse(new ArrayList<>()));
            } finally {
                SqlSessionUtils.closeSqlSession(sqlSession, sqlSessionFactory);
            }
            BaseDataLog.DATA_CHANGES.get().add(change);
        }
        // 删除
        if (SqlCommandType.DELETE.equals(mappedStatement.getSqlCommandType())) {
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获取sql语句开头部分
     *
     * @param sql ignore
     * @return ignore
     */
    private int indexOfSqlStart(String sql) {
        String upperCaseSql = sql.toUpperCase();
        Set<Integer> set = new HashSet<>();
        set.add(upperCaseSql.indexOf("SELECT "));
        set.add(upperCaseSql.indexOf("UPDATE "));
        set.add(upperCaseSql.indexOf("INSERT "));
        set.add(upperCaseSql.indexOf("DELETE "));
        set.remove(-1);
        if (CollectionUtils.isEmpty(set)) {
            return -1;
        }
        List<Integer> list = new ArrayList<>(set);
        list.sort(Comparator.naturalOrder());
        return list.get(0);
    }


}
