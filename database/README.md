# 数据库初始化脚本

本目录包含 HAUST Exchange Platform 的数据库初始化脚本。

## 📁 文件说明

| 文件名 | 说明 |
|--------|------|
| `schema.sql` | 完整的数据库结构和初始数据脚本 |

## 🗄️ 数据库结构

### 数据库信息

- **数据库名**: `haust_exchange_platform`
- **字符集**: `utf8mb4`
- **排序规则**: `utf8mb4_unicode_ci`

### 数据表

#### 1. user (用户表)
存储用户账号信息

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，用户ID |
| account | VARCHAR(100) | 用户账号（唯一） |
| password | VARCHAR(255) | 加密密码（BCrypt） |
| role | TINYINT | 角色：0=管理员，1=普通用户，2=问题账户 |

#### 2. user_monitor (用户监控表)
追踪用户登录行为

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| account | VARCHAR(100) | 用户账号 |
| login_times | INT | 登录次数 |
| ip | VARCHAR(50) | IP地址 |
| create_time | DATETIME | 创建时间 |

#### 3. coding_sharing (内推信息表)
存储内推信息

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键ID |
| user_id | BIGINT | 用户ID |
| company_name | VARCHAR(200) | 公司名称 |
| detail | TEXT | 详细内推信息 |
| remark | TEXT | 备注信息 |
| recommander_email | VARCHAR(255) | 推荐者邮箱 |
| click_number | INT | 浏览次数 |
| recommand_index | TINYINT | 推荐指数（1-5） |
| status | TINYINT | 状态：0=未审核，1=已通过，-1=未通过 |
| code_id | VARCHAR(100) | 内推码 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

#### 4. post (帖子表)
存储论坛帖子

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，帖子ID |
| title | VARCHAR(255) | 帖子标题 |
| description | TEXT | 帖子详细信息 |
| user_id | BIGINT | 用户ID |
| latest_answer_id | BIGINT | 最新回答ID |
| answer_times | INT | 回答数量 |
| anonymity | BOOLEAN | 是否匿名 |
| click_count | BIGINT | 浏览次数 |
| liked_times | INT | 点赞数量 |
| create_time | DATETIME | 提问时间 |
| update_time | DATETIME | 更新时间 |

#### 5. post_reply (帖子评论表)
存储帖子评论和回复

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 帖子回答ID |
| post_id | BIGINT | 帖子ID |
| answer_id | BIGINT | 回复的上级回答ID |
| user_id | BIGINT | 回答者ID |
| content | TEXT | 回答内容 |
| target_user_id | BIGINT | 回复的目标用户ID |
| target_reply_id | BIGINT | 回复的目标回复ID |
| reply_times | INT | 评论数量 |
| liked_times | INT | 点赞数量 |
| anonymity | BOOLEAN | 是否匿名 |
| create_time | DATETIME | 创建时间 |
| update_tsime | DATETIME | 更新时间 |

## 🚀 使用方法

### 方法一：命令行执行

```bash
# 在项目根目录执行
mysql -u root -p < database/schema.sql

# 或指定用户
mysql -u your_username -p < database/schema.sql
```

### 方法二：MySQL 客户端

```bash
# 登录 MySQL
mysql -u root -p

# 执行脚本
mysql> source /path/to/HaustExchangePlatform/database/schema.sql
```

### 方法三：图形化工具

使用 MySQL Workbench、Navicat 等工具：
1. 连接到 MySQL 服务器
2. 打开 `schema.sql` 文件
3. 执行脚本

## 🔐 默认账号

脚本会自动创建一个管理员账号：

```
账号: admin
密码: admin123
角色: 管理员 (role=0)
```

⚠️ **重要提示**: 首次登录后请立即修改密码！

## ✅ 验证安装

执行以下 SQL 验证数据库是否正确创建：

```sql
-- 切换到数据库
USE haust_exchange_platform;

-- 查看所有表
SHOW TABLES;

-- 应该看到 5 个表：
-- +----------------------------------+
-- | Tables_in_haust_exchange_platform|
-- +----------------------------------+
-- | coding_sharing                   |
-- | post                             |
-- | post_reply                       |
-- | user                             |
-- | user_monitor                     |
-- +----------------------------------+

-- 验证管理员账号
SELECT id, account, role FROM user WHERE role = 0;

-- 应该看到管理员账号
-- +----+---------+------+
-- | id | account | role |
-- +----+---------+------+
-- |  1 | admin   |    0 |
-- +----+---------+------+
```

## 🔄 重新初始化

如果需要重新初始化数据库（⚠️ 会删除所有数据）：

```sql
-- 删除数据库
DROP DATABASE IF EXISTS haust_exchange_platform;

-- 然后重新执行 schema.sql 脚本
```

## 📝 注意事项

1. **字符集**: 使用 `utf8mb4` 支持完整的 Unicode 字符（包括 emoji）
2. **时区**: 建议 MySQL 时区设置为 `Asia/Shanghai` 或 `+08:00`
3. **存储引擎**: 使用 InnoDB，支持事务和外键
4. **密码加密**: 用户密码使用 BCrypt 加密存储
5. **索引优化**: 已为常用查询字段添加索引

## 🔧 配置建议

### MySQL 配置优化

在 `my.cnf` 或 `my.ini` 中建议配置：

```ini
[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
default-time-zone='+08:00'
max_connections=1000
innodb_buffer_pool_size=1G
```

### 连接字符串

在应用配置中使用：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/haust_exchange_platform?useUnicode=true&characterEncoding=utf8mb4&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

## 📚 相关文档

- [DEPLOYMENT.md](../DEPLOYMENT.md) - 完整部署指南
- [QUICKSTART.md](../QUICKSTART.md) - 快速启动指南
- [README.md](../README.md) - 项目说明

## 🐛 常见问题

### 问题1：字符集错误

```sql
-- 如果遇到字符集问题，执行：
ALTER DATABASE haust_exchange_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 问题2：权限不足

```sql
-- 如果遇到权限问题，授予用户权限：
GRANT ALL PRIVILEGES ON haust_exchange_platform.* TO 'your_user'@'localhost';
FLUSH PRIVILEGES;
```

### 问题3：表已存在

脚本使用 `DROP TABLE IF EXISTS`，会自动删除已存在的表。如果遇到问题，可以手动删除：

```sql
DROP TABLE IF EXISTS post_reply;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS coding_sharing;
DROP TABLE IF EXISTS user_monitor;
DROP TABLE IF EXISTS user;
```

## 📞 获取帮助

如果遇到问题：
- 查看 [DEPLOYMENT.md](../DEPLOYMENT.md) 的常见问题部分
- 提交 [Issue](https://github.com/qmhwx666/HaustExchangePlatform/issues)
- 联系作者：QQ 3225483474
