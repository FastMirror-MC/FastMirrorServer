package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Cores
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.ktorm.dsl.and
import org.ktorm.dsl.eq

abstract class Download {
    @ApiModel(description = "下载资源请求")
    class Param (
        name: String,
        version: String,
        build: String,
    ): IParams {
        @ApiModelProperty("核心名")
        val name: String
        @ApiModelProperty("游戏版本")
        val version: String
        @ApiModelProperty("核心构建号/版本号")
        val build: String
        init {
            this.name = name.lowercase()
            this.version = version.lowercase()
            this.build = build.lowercase()
        }
        override fun query(alias: Cores)
              = (alias.name eq name) and
                (alias.version eq version) and
                (alias.build eq build)
    }

    data class Response (
        val artifact: String,
        val sha1: String,
        val url: String
        ): IResponse
}