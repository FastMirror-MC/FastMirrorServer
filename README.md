# FastMirrorServer HTTP API文档

# 目录
<a name="catalog"></a>

| Http request                                                       | Description       |
|--------------------------------------------------------------------|-------------------|
| [**GET** /api/v3](/#summary)                                       | 获取支持的服务端列表        |
| [**GET** /api/v3/{name}](/#project_info)                           | 获取服务端部分信息         |
| [**GET** /api/v3/{name}/{mc_version}](/#project_mc_version_info)   | 获取获取对应游戏版本的构建版本列表 |
| [**GET** /api/v3/{name}/{mc_version}/{core_version}](/#metadata)   | 获取指定核心信息          |
| [**GET** /download/{name}/{mc_version}/{core_version}](/#download) | 下载文件              |

# 请求返回值
请求的返回值有统一的格式。

```json5
{
  "data": [                           // 返回的数据
    {
        "name": "Arclight",
        "tag": "mod",
        "recommend": true
    }
  ],
  "code": "fin::success",             // 请求错误码，请求成功时无意义
                                      // 你也可以通过判断这个码是否为`fin::success`来确定是否成功
  "success": true,                    // 请求是否成功
  "message": "Request successfully."  // 错误信息，请求成功时无意义。
}
```
需要特别注意的是，`.data`的类型不固定，例如部分接口返回的是一个数组，而部分接口返回的是一个对象或别的值。

请求失败时，`.data`**一般**都是null。出现内部错误时(http status code == 500)，`.data`会携带错误信息，此时将整个请求(包括请求的url、method等)发送给网站管理员，或直接给本项目开issue。

以下的文档中，**返回值**一栏均为`.data`中的内容。

# 请求限额
普通用户每小时有200次的请求限额;

注册用户每小时有500次的请求限额.

该限额以后可能会进行调整

# API

## 1. /api/v3
<a name="summary"></a>
获取支持的服务端列表
### 请求方法
GET
### 参数
*别问，问就是个patch*

*参数的返回值参考下一个接口*

| position | optional | name    | type  | description      |
|----------|----------|---------|-------|------------------|
| query    | true     | project | array | 查询多个服务端支持的游戏版本列表 |

### 返回
**Content-Type**: application/json

```json5
[
  {
    "name": "Arclight",   // 核心的名称
    "tag": "mod",         // 分类，例如mod\pure\proxy等。
    "recommend": true     // 是否推荐使用
  }
  /*...*/
]
```
### 身份认证

无

## 2. /api/v3/{name}
<a name="project_info"></a>
获取服务端部分信息
### 请求方法
GET
### 参数
| position | optional | name | type   | description |
|----------|----------|------|--------|-------------|
| path     | false    | name | string | 服务端的名称      |

### 返回
**Content-Type**: application/json
```json5
{
  "name": "Arclight",
  "tag": "mod",
  "homepage": "https://github.com/IzzelAliz/Arclight",
  "mc_versions": [
    "horn", 
    "GreatHorn", 
    "1.18", 
    "1.17", 
    "1.16", 
    "1.15"
  ]
}
```
### 身份认证

无

## 3. /api/v3/{name}/{mc_version}
<a name="project_mc_version_info"></a>
获取获取对应游戏版本的构建版本列表
### 请求方法
GET
### 参数
| position | optional | name       | type   | description |
|----------|----------|------------|--------|-------------|
| path     | false    | name       | string | 服务端的名称      |
| path     | false    | mc_version | string | mc版本        |
| query    | true     | offset     | int    | 从第几个开始查询    |
| query    | true     | count      | int    | 查询的数量       |

### 返回
**Content-Type**: application/json
```json5
{
  "builds": [{
    "name": "Arclight",
    "mc_version": "1.18",     // mc版本
    "core_version": "1.0.8",  // 核心文件版本
    "update_time": "2023-02-02T07:50:37", // 更新时间，UTC
    "sha1": "6922a497d8d3204345d5fe83c04bbec4a6f456b6"  // 文件校验值
  }],
  "offset": 0,  // 从第几个开始查询
  "limit": 1,   // 查询的数量
  "count": 9    // 总个数
}
```
### 身份认证

无

## 4. /api/v3/{name}/{mc_version}/{core_version}
<a name="metadata"></a>
获取指定核心信息
### 请求方法
GET
### 参数
| position | optional | name         | type   | description |
|----------|----------|--------------|--------|-------------|
| path     | false    | name         | string | 服务端的名称      |
| path     | false    | mc_version   | string | mc版本        |
| path     | false    | core_version | string | 构建版本        |

### 返回
**Content-Type**: application/json
```json5
{
  "name": "Arclight",
  "mc_version": "1.18",
  "core_version": "1.0.8",
  "update_time": "2023-02-02T07:50:37",
  "sha1": "6922a497d8d3204345d5fe83c04bbec4a6f456b6",
  "filename": "Arclight-1.18-1.0.8.jar",
  "download_url": "http://localhost/download/Arclight/1.18/1.0.8"
}
```
### 身份认证

无

## 5. /download/{name}/{mc_version}/{core_version}
<a name="download"></a>
下载文件

建议使用 [/api/v3/{name}/{mc_version}/{core_version}](/#metadata)来获取下载地址，而不是手动拼接url。

### 请求方法
GET
### 参数
| position | optional | name         | type   | description |
|----------|----------|--------------|--------|-------------|
| path     | false    | name         | string | 服务端的名称      |
| path     | false    | mc_version   | string | mc版本        |
| path     | false    | core_version | string | 构建版本        |

### 返回
**Content-Type**: application/octet-stream

### 身份认证

无
