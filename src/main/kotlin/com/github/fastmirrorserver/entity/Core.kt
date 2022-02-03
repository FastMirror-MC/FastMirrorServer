package com.github.fastmirrorserver.entity

import org.ktorm.dsl.isNotNull
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.datetime
import org.ktorm.schema.varchar
import java.time.LocalDateTime

interface Core : Entity<Core> {
    companion object: Entity.Factory<Core>()
    var name: String
    var version: String
    var build: String
    var update: LocalDateTime
    var release: Boolean
    var sha1: String
}

open class Cores(alias: String?) : Table<Core>("t_core", alias) {
    companion object : Cores(null)
    override fun aliased(alias: String) = Cores(alias)

    val name = varchar("name").primaryKey().bindTo { it.name }
    val version = varchar("version").primaryKey().bindTo { it.version }
    val build = varchar("build").primaryKey().bindTo { it.build }
    val release = boolean("release").bindTo { it.release }
    val update = datetime("update").bindTo { it.update }
    val sha1 = varchar("sha1").bindTo { it.sha1 }
}
