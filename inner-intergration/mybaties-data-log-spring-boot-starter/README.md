# 数据操作日志
使用Spring AOP + mybatis插件实现拦截数据库操作并根据不同需求进行数据对比分析，主要适用于系统中需要对数据操作进行记录、在更新数据时准确记录更新字段

核心：AOP、mybatis插件（拦截器）、mybatis-Plus实体规范、数据对比

从底层具体执行的 sql 语句入手，通过解析表名及更新条件来构造数据更新前后的查询sql，再使用Spring AOP对方法执行前后进行处理，记录更新前后的数据。最后再使用java反射机制将数据更新前后进行对比记录。

注：

使用AOP涉及到一点，就是需要保证AOP与Spring 数据库事务之间的执行顺序，如果AOP先执行然后再提交事务，那结果则是数据无变化。

#使用操作
##1、数据库添加表结构
```
DROP TABLE IF EXISTS `data_log`;
CREATE TABLE `data_log` (
  `id` varchar(36) COLLATE utf8_bin NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `create_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `enable_flag` bit(1) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_user_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `update_user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `content` varchar(8000) COLLATE utf8_bin DEFAULT NULL,
  `fk_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
```
##2、在使用项目中添加引用
````
    <dependency>
        <groupId>com.key.win</groupId>
        <artifactId>mybaties-data-log-spring-boot-starter</artifactId>
    </dependency>
````
##3、在使用方法上添加@DataLog注解，例：
````
    @DataLog
    @PostMapping("/emergencySaveOrUpdate")
    @ApiOperation("应急事件管理保存")
    public Result<DailyReportNetOperationMain> dailyReportNetOperationMainSaveOrUpdate(DailyReportNetOperationMain dailyReportNetOperationMain) {
        BaseDataLog.FK_ID.set(dailyReportNetOperationMain.getId());
        return dailyReportNetOperationMainService.saveOrUpdateBean(dailyReportNetOperationMain);
    }
    //设置日志的外键Id
    BaseDataLog.FK_ID.set(user.getId());
````