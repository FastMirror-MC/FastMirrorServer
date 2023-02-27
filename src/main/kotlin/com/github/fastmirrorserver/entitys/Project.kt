package com.github.fastmirrorserver.entitys

import com.github.fastmirrorserver.entitys.Cores.bindTo
import org.ktorm.dsl.QueryRowSet
import org.ktorm.entity.Entity
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.varchar

interface Project : Entity<Project> {
    val name: String
    val url: String
    val tag: String
    val recommend: Boolean
}

object Projects : Table<Project>("t_project") {
    val name = varchar("name").primaryKey().bindTo { it.name }
    val url = varchar("url").bindTo { it.url }
    val tag = varchar("tag").bindTo { it.tag }
    val recommend = boolean("recommend").bindTo { it.recommend }
}
