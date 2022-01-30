# FastMirrorServer

## 接口文档 - Web端

### 1. 核心信息

#### 1. 获取服务端核心最新更新信息

  **url**: /serverjar/latest  
  **method**: GET  
  **params**:  

  | key      | type   | description |
  | -------- | ------ | ----------- |
  | names    | string | 核心名称    |
  | versions | string | mc版本      |
  
  **bodyType**: json  
  **example**:  
```json
{
    "Spigot": {      // 核心名称
        "1.18.1": {  // mc版本
            "latestReleaseBuilds":  "3413",                 // 最新正式版构建号
            "latestReleaseUpdate":  "2022-02-25T22:37:00Z", // 最新正式版更新时间, UTC
            "latestBuilds":         "3413",                 // 最新构建构建号
            "latestUpdate":         "2022-02-25T22:37:00Z"  // 最新构建更新时间, UTC
        }
    }
}
```
#### 2. 获取服务端心历史更新信息

  **url**: /serverjar/history  
  **method**: GET  
  **params**:

  | key      | type   | description  |
  | -------- | ------ | ------------ |
  | names    | string | 核心名称     |
  | versions | string | mc版本       |
  | offset   | int    | 分页开始位置 |
  | limit    | int    | 分页长度     |
  
  **bodyType**: json  
  **example**:  
```json
{
    "Spigot": {      // 核心名称
        "1.18.1": [  // mc版本
            {
                "builds":  "3413",   // 版本
                "update":  "2022-02-25T22:37:00Z",   // 更新时间, UTC
                "release": true   // 是否为正式版
            }
        ]
    }
}
```

### 2. 核心下载

#### 1. 核心文件下载链接

**url**: /download/link  
**method**: GET  
**params**:

| key     | type   | description |
| ------- | ------ | ----------- |
| name    | string | 核心名称    |
| build   | string | 核心构建号  |
| version | string | mc版本      |

**bodyType**: json  
**example**:  
```json
{
    "artifact":   "spigot-1.18.1-3413.jar",                     // 文件名，带后缀
    "hash":       "862678eabf78c1fc309e4b9cd1c38515712e7ada",   // 文件校验码
    "url":        "/download/..."                               // 下载url
}
```
#### 2. 核心文件下载

以下只是说明下载文件的url的构造，不需要自己组装  
计划使用nginx的secure_link模块来实现防盗链功能

**url**: /download/jar?token=  
**method**: GET  
**bodyType**: octet-stream  

### 3. 其他信息
#### 1. 核心版本列表
**url**: /names
#### 2. mc版本列表
**url**: /versions


## 接口文档 - 采集站端
采集站要统一用密钥来做身份认证  
至于这个密钥怎么来, 我的计划是服务端生成一份token，发送给采集站。采集站提交的时候带上clientId和token做鉴权和记录。  
token为一次性的，每次提交后都需要更新。同时，token需要保持一个每天更新一次的频率，这个更新不受提交的影响。  
token的发送工作由ssh完成。  

### 提交数据
clientId - 采集端id，各采集端尽量不同且唯一
name - 核心名称

  **url**: /submit/{clientId}/{name}  
  **method**: POST  
  **params**:

  | key        | type   | description                        |
  | ---------- | ------ | ---------------------------------- |
  | token      | string | 采集站的token                      |
  | builds     | int    | 核心构建号                         |
  | release    | bool   | 是否为正式版                       |
  | version    | string | 对应的mc版本                       |
  | updateTime | string | 核心更新的时间                     |
  | hash       | string | 文件校验码，要求该校验码为原站提供 |
  | file       | stream | 核心文件                           |
  
  **bodyType**: text
