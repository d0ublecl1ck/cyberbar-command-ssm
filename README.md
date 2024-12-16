# CyberBarCommand

## 项目简介
CyberBarCommand 是一个基于 Spring MVC + MyBatis 的现代化网吧管理系统后端服务。系统提供完整的网吧运营管理功能，包括区域管理、机器管理、用户管理、管理员管理、订单管理等核心功能，采用 RESTful API 设计规范。

## 技术栈
- Spring MVC 5.3.20
- MyBatis 3.5.11
- MySQL 8.0
- Swagger 2.9.2
- Jackson 2.14.2
- Lombok
- PageHelper
- JDK 19

## 主要功能
- 区域管理（网吧区域划分）
- 机器管理（电脑设备管理）
- 用户管理（会员管理）
- 管理员系统
- 订单管理（商品订单）
- RESTful API 接口
- 统一的日期时间处理
- 分页查询支持
- API 文档自动生成
- 跨域(CORS)支持

## 项目结构
```
src/
├── main/
│   ├── java/
│   │   └── org/example/
│   │       ├── config/         # 配置类
│   │       ├── controller/     # 控制器
│   │       ├── dto/           # 数据传输对象
│   │       ├── entity/        # 实体类
│   │       ├── exception/     # 异常处理
│   │       ├── mapper/        # 数据访问层
│   │       └── service/       # 业务逻辑层
│   └── resources/
│       ├── mapper/           # MyBatis映射文件
│       ├── applicationContext.xml  # Spring配置
│       ├── logback.xml       # 日志配置
│       └── spring-mvc.xml   # SpringMVC配置
```

## 配置说明

### 数据库配置
数据库连接配置位于 `applicationContext.xml`:
```xml
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="url" value="jdbc:mysql://localhost:3306/cyberhub"/>
    <property name="username" value="root"/>
    <property name="password" value="password"/>
</bean>
```

### 跨域配置
系统支持跨域访问，配置位于 `spring-mvc.xml`:
- 允许源: http://localhost:3000, http://localhost:3001
- 允许方法: GET, POST, PUT, DELETE, OPTIONS
- 允许凭证: true
- 最大年龄: 3600秒

### Swagger API文档
- 访问地址: `/swagger-ui.html`
- API标题: Zone API文档
- 版本: 1.0

### JWT配置
系统使用JWT进行身份验证:
- 密钥配置: 在 `application.properties` 中配置 `jwt.secret`
- 过期时间: 默认24小时，可通过 `jwt.expiration` 配置
- Token格式: `Bearer <token>`
- 排除路径: 登录接口和Swagger文档不需要认证

### 订单管理
系统支持完整的订单管理功能，包括余额扣除和库存管理：
- **创建订单**：
  - 支持多商品订单创建
  - 自动扣减商品库存
  - 库存不足时创建失败
- **订单状态**：
  - Pending（待处理）
  - Completed（已完成）
  - Cancelled（已取消）
- **订单查询**：支持多条件组合查询
  - 按商品名称
  - 按日期范围
  - 按订单状态
  - 按用户ID
  - 按机器ID
- **余额和库存管理**：
  - 创建订单时自动扣除用户余额和商品库存
  - 如果用户余额不足或库存不足，订单创建将失败
  - 订单状态更新时，系统将自动处理库存变化，防止超卖

## 开发指南

### 环境要求
- JDK 19+
- Maven 3.6+
- MySQL 8.0+

### 本地开发设置

1. 克隆项目
```bash
git clone [repository-url]
```

2. 配置数据库连接信息（applicationContext.xml）

3. 启动项目
```bash
mvn clean package
mvn tomcat7:run
```

### 代码规范

1. 命名规范
- 类名：大驼峰命名
- 方法名：小驼峰命名
- 变量名：小驼峰命名
- 常量：全大写下划线分隔

2. 注释规范
- 类注释：说明类的用途
- 方法注释：说明方法的功能、参数和返回值
- 关键代码注释：说明复杂逻辑

## 监控与维护

### 日志管理
- 日志位置：logs/application.log
- 日志级别：INFO（可在logback.xml中调整）
- 日志轮转：按天轮转，保留30天
- 日志格式：时间戳 [线程] 日志级别 类名 - 消息
- 字符编码：UTF-8（支持中文日志）
- 日志分割：按大小（10MB）和时间（每天）自动分割
- 控制台和文件双重输出

### 事务管理
- 使用Spring声明式事务
- 关键业务操作（如订单创建、状态更新）自动开启事务
- 异常时自动回滚

### 库存管理
- 支持并发库存操作
- 库存不足时订单创建失败
- 订单状态变更时自动处理库存
- 防止超卖

## 版本历史

### v1.0.0
- 初始版本发布
- 实现基础功能模块
- 完成API文档
- 实现订单库存联动管理
