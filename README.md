# FastMirrorServer HTTP API文档

<a name="catalog"></a>

# 目录

| Class                | Method                                               | Http request                                            | Description                        |
| -------------------- | ---------------------------------------------------- | ------------------------------------------------------- | ---------------------------------- |
| *DetailsController*  | [**summary**](/#DetailsController-summary)           | **
GET** /api/v2                                         | 获取支持的服务端列表               |
| *DetailsController*  | [**versions**](/#DetailsController-versions)         | **
GET** /api/v2/all                                     | 获取所有服务端支持的MC版本列表     |
| *DetailsController*  | [**version**](/#DetailsController-version)           | **
GET** /api/v2/{name}                                  | 获取服务端支持的游戏版本列表       |
| *DetailsController*  | [**coreVersions**](/#DetailsController-coreVersions) | **
GET** /api/v2/{name}/{version}                        | 获取获取对应游戏版本的构建信息列表 |
| *DetailsController*  | [**artifact**](/#DetailsController-artifact)         | **
GET** /api/v2/{name}/{version}/{coreVersion}          | 获取指定核心信息                   |
| *DetailsController*  | [**download**](/#DetailsController-download)         | **
GET** /api/v2/{name}/{version}/{coreVersion}/download | 获取核心下载所需信息               |
| *DownloadController* | [**download**](/#DownloadController-download)        | **
GET** /download                                       | 用于下载文件和获取文件下载链接     |
| *UtilController*     | [**homepage**](/#UtilController-homepage)            | **
GET** /{name}/homepage                                | 获取服务端官方主页                 |

# 1. DetailsController

<a name="DetailsController-summary"></a>

## 2.1 **summary**

> 获取支持的服务端列表

### 参数

无

### 返回

样例：

```json
[
    {
        "name":"Arclight",
        "url":"https://github.com/IzzelAliz/Arclight",
        "tag":"mod",
        "recommend":true
    }
]
```

| 参数名    | 参数类型 | 描述                                 |
| --------- | -------- | ------------------------------------ |
| name      | String   | 核心的项目名称，也是常用名称         |
| url       | String   | 项目官方主页(如果有)或GitHub项目地址 |
| tag       | String   | 核心分类                             |
| recommend | Boolean  | 推荐标签，为true时会在前端显示星标   |

### 身份认证

无

### HTTP请求头

- Content-Type: Not defined
- Accept: application/json

<a name="DetailsController-versions"></a>

## 2.2 **versions**

> 获取所有服务端支持的MC版本列表  
> 该接口为下一接口的集合

### 参数

无

### 返回

样例：

```json
{
    "Arclight":[
        "1.18",
        "1.17",
        "1.16",
        "1.15"
    ]
}
```

| 参数名 | 参数类型     | 描述                                                                   |
| ------ | ------------ | ---------------------------------------------------------------------- |
| {name} | List[String] | {name}为核心的名称，List为对应的MC版本。该项受采集端提交的数据的影响。 |

### 身份认证

无

### HTTP请求头

- Content-Type: Not defined
- Accept: application/json

<a name="DetailsController-version"></a>

## 2.3 **version**

> 获取指定服务端支持的游戏版本列表

### 参数

无

### 返回

样例：

```json
[
    "1.18",
    "1.17",
    "1.16",
    "1.15"
]
```

| 参数名 | 参数类型 | 描述                                         |
| ------ | -------- | -------------------------------------------- |
| ------ | String   | 对应的MC版本。该项受采集端提交的数据的影响。 |

### 身份认证

无

### HTTP请求头

- Content-Type: Not defined
- Accept: application/json

<a name="DetailsController-coreVersions"></a>

## 2.4 **coreVersions**

> 获取获取对应游戏版本的构建信息列表

### 参数

无

### 返回

样例：

```json
{
    "builds": [
        {
            "name": "Arclight",
            "version": "1.18",
            "coreVersion": "build69",
            "update": "2022-03-11T05:02:36.022997Z"
        },
        {
            "name": "Arclight",
            "version": "1.18",
            "coreVersion": "build54",
            "update": "2022-02-15T05:39:38.747863Z"
        },
        {
            "name": "Arclight",
            "version": "1.18",
            "coreVersion": "1.0.0",
            "update": "2021-12-30T05:49:46Z"
        }
    ],
    "offset": 0,
    "limit": 25,
    "count": 3
}
```

| 参数名 | 参数类型   | 描述                                         |
| ------ | ---------- | -------------------------------------------- |
| builds | List[Item] | 对应的MC版本。该项受采集端提交的数据的影响。 |
| offse  | Integer    | 偏移量，表示从第几个元素开始。用于分页。     |
| limit  | Integer    | 本次请求期望获取的数据数量。用于分页。       |
| count  | Integer    | 本次请求实际获取的数据数量。用于分页。       |

**Item**:
| 参数名 | 参数类型 | 描述 | | ----------- | -------- | ---------------------------------------------------------------- | | name
| String | 核心的项目名称，也是常用名称。 | | version | String | MC版本。该项受采集端提交的数据的影响。 | | coreVersion | String |
核心版本。若开发团队未提供版本信息，则以`build${构建号}`做版本。 | | update | String | 发布该版本的UTC时间。 |

### 身份认证

无

### HTTP请求头

- Content-Type: Not defined
- Accept: application/json

<a name="DetailsController-artifact"></a>

## 2.5 **artifact**

> 获取指定服务端支持的游戏版本列表

### 参数

无

### 返回

样例：

```json
{
    "name": "Arclight",
    "version": "1.18",
    "coreVersion": "build69",
    "update": "2022-03-11T05:02:36.022997Z"
}
```

| 参数名      | 参数类型 | 描述                                                             |
| ----------- | -------- | ---------------------------------------------------------------- |
| name        | String   | 核心的项目名称，也是常用名称。                                   |
| version     | String   | MC版本。该项受采集端提交的数据的影响。                           |
| coreVersion | String   | 核心版本。若开发团队未提供版本信息，则以`build${构建号}`做版本。 |
| update      | String   | 发布该版本的UTC时间。                                            |

### 身份认证

无

### HTTP请求头

- Content-Type: Not defined
- Accept: application/json

<a name="DetailsController-download"></a>

## 2.6 **download**

> 获取指定服务端支持的游戏版本列表

### 参数

无

### 返回

样例：

```json
{
    "name": "Arclight",
    "version": "1.18",
    "coreVersion": "build69",
    "update": "2022-03-11T05:02:36.022997Z",
    "artifact": "Arclight-1.18-build69.jar",
    "sha1": "de2be0e7bb19e545489a3baf9021c2f26357db43",
    "url": "/download?token=CAD27B0DC11E4E0980B7E8EDA1D97149"
}
```

| 参数名      | 参数类型 | 描述                                                             |
| ----------- | -------- | ---------------------------------------------------------------- |
| name        | String   | 核心的项目名称，也是常用名称。                                   |
| version     | String   | MC版本。该项受采集端提交的数据的影响。                           |
| coreVersion | String   | 核心版本。若开发团队未提供版本信息，则以`build${构建号}`做版本。 |
| update      | String   | 发布该版本的UTC时间。                                            |
| artifact    | String   | 文件名。                                                         |
| sha1        | String   | 文件SHA-1校验值。                                                |
| url         | String   | 文件下载地址。                                                   |

### 身份认证

无

### HTTP请求头

- Content-Type: Not defined
- Accept: application/json

# 2. DownloadController

<a name="DownloadController-download"></a>

## 2.1 **download**

> 用于下载文件和获取文件下载链接  
> 具有最简单的防盗链功能  
> 一般来说不用也不能由客户端直接拼接url，因此该接口的文档仅做参考

### 参数

| 参数名 | 描述              |
| ------ | ----------------- |
| token  | 请求的文件的token |

### 返回

二进制流

### 身份认证

无

### HTTP请求头

- Content-Type: Not defined
- Accept: application/octet-stream

# 3. UtilController

<a name="UtilController-homepage"></a>

## 3.1 **homepage**

> 获取服务端官方主页  
> 但是这个功能和`/api/v2`是重合的

### 参数

无

### 返回

样例：

```json
{
    "name":"Arclight",
    "url":"https://github.com/IzzelAliz/Arclight",
    "tag":"mod",
    "recommend":true
}
```

| 参数名    | 参数类型 | 描述                                 |
| --------- | -------- | ------------------------------------ |
| name      | String   | 核心的项目名称，也是常用名称         |
| url       | String   | 项目官方主页(如果有)或GitHub项目地址 |
| tag       | String   | 核心分类                             |
| recommend | Boolean  | 推荐标签，为true时会在前端显示星标   |

### 身份认证

无

### HTTP请求头

- Content-Type: Not defined
- Accept: application/json

