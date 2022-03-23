package com.github.fastmirrorserver.entity

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.varchar

data class Project(
    val name: String,
    val url: String,
    val tag: String,
    val recommend: Boolean
)

object Projects : BaseTable<Project>("t_project") {
    val id = varchar("id").primaryKey()
    val url = varchar("url")
    val tag = varchar("tag")
    val recommend = boolean("recommend")
    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Project(
        name = row[id]!!,
        url = row[url]!!,
        tag = row[tag]!!,
        recommend = row[recommend]!!
    )
}
