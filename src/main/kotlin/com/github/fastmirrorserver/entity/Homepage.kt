package com.github.fastmirrorserver.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.varchar

@ApiModel("服务端官方主页")
data class Homepage (
    @ApiModelProperty("服务端名称", example = "Arclight")
    val name: String,
    @ApiModelProperty("主页网址", example = "https://github.com/IzzelAliz/Arclight")
    val url: String
)

object Homepages : BaseTable<Homepage>("t_homepage") {
    val id = varchar("id").primaryKey()
    val url = varchar("url")
    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Homepage(
        name = row[id]!!,
        url = row[url]!!
    )
}
