package com.github.fastmirrorserver.dto

import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.getFilename
import com.github.fastmirrorserver.getPath
import org.ktorm.dsl.and
import org.ktorm.dsl.eq

open class CoreInfo (
    val name: String,
    val version: String,
    val coreVersion: String,
) {
    val path: String = "./core/${getPath(name, version, coreVersion)}"
    val filename: String = getFilename(name, version, coreVersion)

    fun toQueryExpression() = toQueryExpression(Cores)
    fun toQueryExpression(it: Cores) = (it.name eq name) and (it.version eq version) and (it.coreVersion eq coreVersion)
}