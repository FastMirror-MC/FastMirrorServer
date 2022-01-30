package com.github.fastmirrorserver.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.enums.CoreType
import com.github.fastmirrorserver.enums.McVersion
import com.github.fastmirrorserver.utc
import org.ktorm.dsl.and
import org.ktorm.dsl.inList
import org.ktorm.expression.ScalarExpression
import java.time.LocalDateTime

abstract class History {
    data class Param(
        val names: String,
        val versions: String,
        val offset: Int,
        val limit: Int
    ) : IParams {
//        private fun name(alias: Cores) = alias.name inList names.split(",").map { CoreType.get(it) }
//        private fun version(alias: Cores) = alias.version inList versions.split(",").map { McVersion.get(it) }

        public fun nameArray() = names.split(",").map { CoreType.get(it).name }.toTypedArray()
        public fun versionArray() = versions.split(",").map { McVersion.get(it).name }.toTypedArray()

        override fun query(alias: Cores) = TODO()
    }

    data class Response(
        @JsonIgnore val name: String,
        @JsonIgnore val version: String,
        val builds: String,
        val update: String,
        val release: Boolean
    ) : IResponse {
        constructor(
            name: CoreType,
            version: McVersion,
            builds: String,
            update: LocalDateTime,
            release: Boolean
        ) : this(
            name = name.toString(),
            version = version.toString(),
            builds = builds,
            update = utc(update),
            release = release
        )
    }
}