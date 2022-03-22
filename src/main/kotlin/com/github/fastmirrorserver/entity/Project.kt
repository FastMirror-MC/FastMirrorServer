package com.github.fastmirrorserver.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.boolean
import org.ktorm.schema.varchar

@ApiModel("服务端官方主页")
data class Project(
    @ApiModelProperty("服务端名称", example = "Arclight")
    val name: String,
    @ApiModelProperty("主页网址", example = "https://github.com/IzzelAliz/Arclight")
    val url: String,
    @ApiModelProperty("服务端标签", example = "mod")
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
