学习笔记
 第 15 节课作业实践
   1、（选做）分析前面作业设计的表，是否可以做垂直拆分。 
   2、（必做）设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表。
   并在新结构在演示常见的增删改查操作。代码、sql 和配置文件，上传到 Github。 
   3、（选做）模拟1000万的订单单表数据，迁移到上面作业2的分库分表中。 
   4、（选做）重新搭建一套4个库各64个表的分库分表，将作业2中的数据迁移到新分库
   
   
   第 16 课作业实践 1、（选做）列举常见的分布式事务，简单分析其使用场景和优缺点。 
   2、（必做）基于hmily TCC或ShardingSphere的Atomikos XA实现一个简单的分布式 事务应用demo（二选一），提交到github。 
   3、（选做）基于ShardingSphere narayana XA实现一个简单的分布式事务demo。 
   4、（选做）基于seata框架实现TCC或AT模式的分布式事务demo。 
   5、（选做☆）设计实现一个简单的XA分布式事务框架demo，只需要能管理和调用2 个MySQL的本地事务即可，不需要考虑全局事务的持久化和恢复、高可用等。 
   6、（选做☆）设计实现一个TCC分布式事务框架的简单Demo，需要实现事务管理器， 不需要实现全局事务的持久化和恢复、高可用等。 
   7、（选做☆）设计实现一个AT分布式事务框架的简单Demo，仅需要支持根据主键id 进行的单个删改操作的SQL或插入操作的事务。
   
作业只完成了15节课必做作业
分库分表使用sharding-proxy实现，从官网下载二进制包，解压到目录
下载地址：https://mirrors.tuna.tsinghua.edu.cn/apache/shardingsphere/5.0.0/apache-shardingsphere-4.1.0-sharding-proxy-bin.tar.gz
修改conf/目录下的server.xml 和 config-sharding.yaml

######################################################################################################

schemaName: sharding_db

dataSourceCommon:
  username: root
  password: root
  connectionTimeoutMilliseconds: 30000
  idleTimeoutMilliseconds: 60000
  maxLifetimeMilliseconds: 1800000
  maxPoolSize: 50
  minPoolSize: 1
  maintenanceIntervalMilliseconds: 30000

dataSources:
  mall0:
    url: jdbc:mysql://127.0.0.1:3306/mall0?serverTimezone=Asia/Shanghai&useSSL=false
  mall1:
    url: jdbc:mysql://127.0.0.1:3306/mall1?serverTimezone=Asia/Shanghai&useSSL=false

rules:
- !SHARDING
  tables:
    t_order:
      actualDataNodes: mall${0..1}.t_order_${0..15}
      tableStrategy:
        standard:
          shardingColumn: order_id
          shardingAlgorithmName: t_order_inline
      keyGenerateStrategy:
        column: order_id
        keyGeneratorName: snowflake
  defaultDatabaseStrategy:
    standard:
      shardingColumn: user_id
      shardingAlgorithmName: database_inline
#  defaultTableStrategy:
#    none:
#  
  shardingAlgorithms:
    database_inline:
      type: INLINE
      props:
        algorithm-expression: mall${user_id % 2}
    t_order_inline:
      type: INLINE
      props:
        algorithm-expression: t_order_${order_id % 16}
  
  keyGenerators:
    snowflake:
      type: SNOWFLAKE
      props:
        worker-id: 123

按照user_id拆分成两个库，按照order_id拆分为16个表
因为本机mysql为8版本，因此把lib/目录下的mysql jar包替换为mysql-connector-java-8.0.21.jar
启动/bin/start.bat
启动sharding_db:mysql -h127.0.0.1 -P3307 -uroot -proot
创建订单表
CREATE TABLE t_order (
  `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `order_no` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `business_id` bigint NOT NULL COMMENT '商家id',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '应付金额',
  `order_status` int DEFAULT NULL COMMENT '订单状态：0->待付款;1->已完成;2->已关闭;3->无效订单',
  `note` varchar(255) DEFAULT NULL COMMENT '订单备注',
  `delete_status` int NOT NULL DEFAULT '0' COMMENT '删除状态：0->未删除;1->已删除',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='订单表'
此时，看shardingsphere-proxy 命令窗口的执行命令，创建两个库的16个表
然后代码测试，连接shardingsphere-proxy 进行增删改查基本操作
