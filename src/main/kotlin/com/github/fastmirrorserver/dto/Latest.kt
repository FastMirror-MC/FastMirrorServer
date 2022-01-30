package com.github.fastmirrorserver.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.enums.CoreType
import com.github.fastmirrorserver.enums.McVersion
import com.github.fastmirrorserver.utc
import org.ktorm.dsl.QueryRowSet
import org.ktorm.dsl.and
import org.ktorm.dsl.inList
import java.time.LocalDateTime

abstract class Latest {
    data class Param(
        val names: String,
        val versions: String
    ) :IParams {
        private fun qName(alias: Cores) = alias.name inList names.split(",").map { CoreType.get(it) }
        private fun qVersion(alias: Cores) = alias.version inList versions.split(",").map { McVersion.get(it) }

        override fun query(alias: Cores) = qName(alias) and qVersion(alias)
    }
    data class ResponseUnit(
        @JsonIgnore
        val name: String,
        @JsonIgnore
        val version: String,
        val latestReleaseBuilds: String,
        val latestReleaseUpdate: String,
        val latestBuilds: String,
        val latestUpdate: String
    ) : IResponse {
        constructor(
            name: CoreType,
            version: McVersion,
            latestReleaseBuilds: String?,
            latestReleaseUpdate: LocalDateTime?,
            latestBuilds: String,
            latestUpdate: LocalDateTime
        ) : this(
            name = name.toString(),
            version = version.toString(),
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