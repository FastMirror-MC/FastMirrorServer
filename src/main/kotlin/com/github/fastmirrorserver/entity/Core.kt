package com.github.fastmirrorserver.entity

import org.ktorm.entity.Entity
import org.ktorm.schema.*
import java.time.LocalDateTime

interface Core : Entity<Core> {
    companion object: Entity.Factory<Core>()
    var name: String
    var version: String
    var coreVersion: String
    var build: Int
    var update: LocalDateTime
    var release: Boolean
    var sha1: String
    var path: String
}

open class Cores(alias: String?) : Table<Core>("t_core", alias) {
    companion object : Cores(null)
    override fun aliased(alias: String) = Cores(alias)

    val name = varchar("name").primaryKey().bindTo { it.name }
    val version = varchar("version").primaryKey().bindTo { it.version }
    val coreVersion = varchar("core_version").primaryKey().bindTo { it.coreVersion }
    val build = int("build").bindTo { it.build }
    val release = boolean("release").bindTo { it.release }
    val update = datetime("update").bindTo { it.update }
    val sha1 = varchar("sha1").bindTo { it.sha1 }
    val path = varchar("path").bindTo { it.path }
}
