package com.github.fastmirrorserver.entity

import com.github.fastmirrorserver.enums.CoreType
import com.github.fastmirrorserver.enums.McVersion
import org.ktorm.entity.Entity
import org.ktorm.entity.EntitySequence
import org.ktorm.entity.map
import org.ktorm.schema.*
import java.time.LocalDateTime

interface Core : Entity<Core> {
    companion object: Entity.Factory<Core>()
    var name: CoreType
    var version: McVersion
    var build: String
    var update: LocalDateTime
    var release: Boolean
    var hash: String
}

open class Cores(alias: String?) : Table<Core>("t_core", alias) {
    companion object : Cores(null)
    override fun aliased(alias: String) = Cores(alias)

    val name = enum<CoreType>("name").primaryKey().bindTo { it.name }
    val version = enum<McVersion>("version").primaryKey().bindTo { it.version }
    val build = varchar("build").primaryKey().bindTo { it.build }
    val release = boolean("release").bindTo { it.release }
    val update = datetime("update").bindTo { it.update }
    val hash = varchar("hash").bindTo { it.hash }
}
