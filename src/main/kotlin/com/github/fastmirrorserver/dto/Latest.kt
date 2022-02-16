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
            this.names = names
            this.versions = versions
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
        val latestReleaseCoreVersion: String,
        @ApiModelProperty("最新稳定版构建的更新时间")
        val latestReleaseUpdateTime: String,
        @ApiModelProperty("最新构建的构建号/版本号")
        val latestCoreVersion: String,
        @ApiModelProperty("最新构建的更新时间")
        val latestUpdateTime: String
    ) : IResponse {
        constructor(
            name: String,
            version: String,
            latestReleaseCoreVersion: String,
            latestReleaseUpdateTime: LocalDateTime,
            latestCoreVersion: String,
            latestUpdateTime: LocalDateTime
        ) : this(
            name = name,
            version = version,
            latestReleaseCoreVersion = latestReleaseCoreVersion,
            latestReleaseUpdateTime = utc(latestReleaseUpdateTime),
            latestCoreVersion = latestCoreVersion,
            latestUpdateTime = utc(latestUpdateTime)
        )
        constructor(
            it: QueryRowSet,
            build: Cores,
            release: Cores
        ) : this (
            name = it[build.name]!!,
            version = it[build.version]!!,
            latestReleaseCoreVersion = it[release.coreVersion]!!,
            latestReleaseUpdateTime = it[release.update]!!,
            latestCoreVersion = it[build.coreVersion]!!,
            latestUpdateTime = it[build.update]!!,
        )
    }
}