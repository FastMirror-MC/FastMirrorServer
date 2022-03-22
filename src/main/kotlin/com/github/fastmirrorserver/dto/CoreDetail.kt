package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Core
import com.github.fastmirrorserver.utc
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "核心信息 - 分页")
data class CoreDetail(
    @ApiModelProperty("服务端构建列表")
    val builds: List<ResponseUnit>,
    val offset: Int,
    val limit: Int,
    @ApiModelProperty("总大小，即max(offset+limit)=count")
    val count: Int
) {
    @ApiModel(description = "核心信息")
    data class ResponseUnit(
        @ApiModelProperty("服务端名称", example = "Arclight")
        val name: String,
        @ApiModelProperty("游戏版本", example = "1.15")
        val version: String,
        @ApiModelProperty("核心版本", example = "1.0.19")
        val coreVersion: String,
        @ApiModelProperty("发布日期(utc)", example = "2021-06-25T00:37:34Z")
        val update: String
    ) : IResponse {
        constructor(o: Core) : this(
            name = o.name,
            version = o.version,
            coreVersion = o.coreVersion,
            update = utc(o.update)
        )
    }
}