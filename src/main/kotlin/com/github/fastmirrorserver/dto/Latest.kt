package com.github.fastmirrorserver.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.utc
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.ktorm.dsl.*
import org.ktorm.expression.BinaryExpression
import org.ktorm.expression.InListExpression
import org.ktorm.expression.ScalarExpression
import java.time.LocalDateTime
import javax.naming.LimitExceededException

abstract class Latest {
    @ApiModel(description = "核心最新数据请求")
    class Param (
        names: String,
        versions: String
    ) : IParams {
        companion object {
            const val MAXIMUM_VERSION = 10
        }
        @ApiModelProperty(value = "核心名称，用逗号分隔，可用all表示全部", example = "")
        val names: String
        @ApiModelProperty(value = "核心版本，用逗号分隔，最多10个，可用all表示全部", example = "")
        val versions: String
        init{
            this.names = names.lowercase()
            this.versions = versions.lowercase()
        }

        private fun queryName(alias: Cores): ScalarExpression<Boolean> {
            if(names == "all") return alias.name.isNotNull()
            return alias.name inList names.split(",")
        }

        private fun queryVersion(alias: Cores): ScalarExpression<Boolean> {
            if(versions == "all") return alias.version.isNotNull()
            val tmp = versions.split(",")
            if(tmp.size > MAXIMUM_VERSION)
                throw LimitExceededException("param `versions` up to $MAXIMUM_VERSION version.")
            return alias.version inList tmp
        }

        override fun query(alias: Cores) = queryName(alias) and queryVersion(alias)
    }
    @ApiModel
    data class ResponseUnit(
        @JsonIgnore
        val name: String,
        @JsonIgnore
        val version: String,
        @ApiModelProperty("最新稳定版构建的构建号/版本号")
        val latestReleaseBuilds: String,
        @ApiModelProperty("最新稳定版构建的更新时间")
        val latestReleaseUpdate: String,
        @ApiModelProperty("最新构建的构建号/版本号")
        val latestBuilds: String,
        @ApiModelProperty("最新构建的更新时间")
        val latestUpdate: String
    ) : IResponse {
        constructor(
            name: String,
            version: String,
            latestReleaseBuilds: String?,
            latestReleaseUpdate: LocalDateTime?,
            latestBuilds: String,
            latestUpdate: LocalDateTime
        ) : this(
            name = name,
            version = version,
            latestReleaseBuilds = latestReleaseBuilds ?: "",
            latestReleaseUpdate = latestReleaseUpdate?.let { utc(it) } ?: "",
            latestBuilds = latestBuilds,
            latestUpdate = utc(latestUpdate)
        )
        constructor(
            it: QueryRowSet,
            build: Cores,
            release: Cores
        ) : this (
            it[build.name]!!,
            it[build.version]!!,
            it[release.build],
            it[release.update],
            it[build.build]!!,
            it[build.update]!!,
        )
    }
}