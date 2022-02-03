package com.github.fastmirrorserver.entity

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.datetime
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.time.LocalDateTime

interface SubmitLog : Entity<SubmitLog> {
    companion object: Entity.Factory<SubmitLog>()
    var client: String
    var name: String
    var version: String
    var build: String
    var timestamp: LocalDateTime
}

object SubmitLogs: Table<SubmitLog>("t_submit_log") {
    val id = int("id").primaryKey()
    val client = varchar("client").bindTo { it.client }
    val name = varchar("name").bindTo { it.name }
    val version = varchar("version").bindTo { it.version }
    val build = varchar("build").bindTo { it.build }
    val timestamp = datetime("time").bindTo { it.timestamp }
}
