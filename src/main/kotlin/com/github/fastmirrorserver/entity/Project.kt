package com.github.fastmirrorserver.entity

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.varchar

interface Project : Entity<Project> {
    val id: String
    val url: String
    val tag: String
    val recommend: Boolean
}

object Projects : Table<Project>("t_project") {
    val id = varchar("id").primaryKey().bindTo { it.id }
    val url = varchar("url").bindTo { it.url }
    val tag = varchar("tag").bindTo { it.tag }
    val recommend = boolean("recommend").bindTo { it.recommend }
}
