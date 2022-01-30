package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.enums.CoreType
import com.github.fastmirrorserver.enums.McVersion
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.expression.ScalarExpression
import java.time.LocalDateTime

abstract class Download {
    data class Param (
        val name: String,
        val version: String,
        val build: String,
    ): IParams {
        override fun query(alias: Cores)
              = (alias.name eq CoreType.get(name)) and
                (alias.version eq McVersion.get(version)) and
                (alias.build eq build)
    }

    data class Response (
        val artifact: String,
        val hash: String,
        val url: String
        ): IResponse
}