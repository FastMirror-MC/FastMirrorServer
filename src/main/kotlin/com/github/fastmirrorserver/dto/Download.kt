package com.github.fastmirrorserver.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "下载资源请求")
data class Download (
    @ApiModelProperty("服务端名")
    val name: String,
    @ApiModelProperty("游戏版本")
    val version: String,
    @ApiModelProperty("服务端版本")
    val coreVersion: String,
    @ApiModelProperty("更新时间")
    val update: String,
    @ApiModelProperty("文件名")
    val artifact: String,
    @ApiModelProperty("SHA-1校验码")
    val sha1: String,
    @ApiModelProperty("下载地址")
    val url: String
): IResponse
