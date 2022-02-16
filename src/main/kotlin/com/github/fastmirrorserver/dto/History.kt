package com.github.fastmirrorserver.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.utc
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime
import javax.naming.LimitExceededException

abstract class History {
    @ApiModel(description = "核心历史数据请求")
    class Param (
        names: String,
        versions: String,
        offset: Int,
        limit: Int = 25
    ) : IParams {
        @ApiModelProperty(value = "核心名称，用逗号分隔", example = "")
        val names: String
        @ApiModelProperty(value = "核心版本，用逗号分隔，最多10个", example = "")
        val versions: String
        @ApiModelProperty(value = "分页偏移量", example = "0")
        val offset: Int
        @ApiModelProperty(value = "当前页数据数量，最多25个，默认25个", required = false, example = "25")
        val limit: Int
        companion object {
            const val MAXIMUM_VERSION = 10
        }
        init {
            this.names = names
            this.versions = versions
            this.offset = if(offset < 0) 0 else offset
            this.limit = if(limit < 0 || limit > 25) 25 else limit
        }

        fun nameArray() = names.split(",")
        fun versionArray (): List<String> {
            val tmp = versions.split(",")
            if(tmp.size > MAXIMUM_VERSION)
                throw LimitExceededException("param `versions` up to ${Latest.Param.MAXIMUM_VERSION} version.")
            return tmp
        }

//        private fun name(alias: Cores) = alias.name inList names.split(",").map { CoreType.get(it) }
//        private fun version(alias: Cores) = alias.version inList versions.split(",").map { McVersion.get(it) }

        override fun query(alias: Cores) = TODO()
    }

    data class Response(
        @JsonIgnore val name: String,
        @JsonIgnore val version: String,
        val coreVersion: String,
        val update: String,
        val release: Boolean
    ) : IResponse {
        constructor(
            name: String,
            version: String,
            coreVersion: String,
            update: LocalDateTime,
            release: Boolean
        ) : this(
            name = name,
            version = version,
            coreVersion = coreVersion,
            update = utc(update),
            release = release
        )
    }
}