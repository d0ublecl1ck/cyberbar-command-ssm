# 网吧管理系统后端服务

## 项目简介

本项目是一个现代化网吧管理系统的后端服务，提供完整的网吧运营管理功能，包括区域管理、机器管理、用户管理、管理员管理等核心功能。系统采用Spring Boot框架开发，提供RESTful API接口，支持前后端分离架构。

## 技术栈

- **框架**: Spring Boot
- **数据库**: MySQL
- **ORM**: MyBatis
- **API文档**: Swagger
- **日志**: Logback
- **工具**: Lombok
- **分页**: PageHelper
- **接口规范**: RESTful API

## 系统架构

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
│       ├── logback.xml      # 日志配置
│       └── spring-mvc.xml   # SpringMVC配置
```

## 核心功能模块

### 1. 区域管理
- 区域的增删改查
- 区域名称唯一性检查

### 2. 机器管理
- 机器的增删改查
- 按区域、价格、状态筛选
- 机器状态监控

### 3. 用户管理
- 用户的增删改查
- 用户状态管理
- 用户统计信息

### 4. 管理员系统
- 管理员登录
- 操作日志记录
- 权限控制

## API文档

### 区域管理API

#### 获取所有区域
```
GET /api/zones
Response: List<Zone>
```

#### 获取单个区域
```
GET /api/zones/{id}
Response: Zone
```

#### 创建区域
```
POST /api/zones
Body: {
    "name": "string"
}
Response: {
    "message": "string"
}
```

#### 更新区域
```
PUT /api/zones/{id}
Body: {
    "name": "string"
}
Response: {
    "message": "string"
}
```

#### 删除区域
```
DELETE /api/zones/{id}
Response: {
    "message": "string"
}
```

### 机器管理API

#### 获取机器列表
```
GET /api/machines
Params:
    zoneId: Integer
    minPrice: BigDecimal
    maxPrice: BigDecimal
    status: String (Abnormal/Idle/Occupied)
Response: List<Machine>
```

#### 获取机器统计
```
GET /api/machines/stats
Response: {
    "totalMachines": Integer,
    "idleMachines": Integer,
    "occupiedMachines": Integer,
    "abnormalMachines": Integer
}
```

### 用户管理API

#### 搜索用户
```
GET /api/users/search
Params:
    keyword: String
    status: String
    pageNum: Integer
    pageSize: Integer
Response: List<User>
```

#### 用户统计
```
GET /api/users/stats
Response: {
    "totalUsers": Integer,
    "onlineUsers": Integer,
    "offlineUsers": Integer,
    "bannedUsers": Integer
}
```

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

2. 配置数据库
```xml
<!-- applicationContext.xml -->
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="url" value="jdbc:mysql://localhost:3306/cyberhub"/>
    <property name="username" value="your-username"/>
    <property name="password" value="your-password"/>
</bean>
```

3. 启动项目
```bash
mvn spring-boot:run
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

3. 异常处理
- 使用全局异常处理器
- 合理使用自定义异常
- 详细记录异常日志

## 部署指南

### 打包
```bash
mvn clean package
```

### 运行
```bash
java -jar target/your-application.jar
```

### 配置说明
- 数据库配置：applicationContext.xml
- 日志配置：logback.xml
- Web配置：spring-mvc.xml

## 监控与维护

### 日志管理
- 日志位置：logs/application.log
- 日志级别：INFO（可在logback.xml中调整）
- 日志轮转：按天轮转，保留30天

### 性能优化
- 使用连接池管理数据库连接
- 合理使用缓存
- 采用分页查询避免大数据量查询

## 常见问题

1. 数据库连接失败
- 检查数据库服务是否启动
- 验证连接信息是否正确
- 确认数据库用户权限

2. 接口返回500错误
- 查看日志文件定位具体错误
- 检查请求参数格式
- 验证数据库操作是否正确

## 更新日志

### v1.0.0
- 初始版本发布
- 实现基础功能模块
- 完成API文档

## 联系与支持

- 项目维护者：[维护者信息]
- 技术支持：[支持渠道]
- 问题反馈：[反馈方式]

## 许可证

[许可证类型]