package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.homepages
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.ktorm.database.Database
import org.ktorm.entity.find
import org.ktorm.support.postgresql.ilike
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api("杂项", description = "核心名称，游戏版本等")
@RestController
@RequestMapping("/api/v2")
class UtilController {
    @Autowired
    lateinit var database: Database

    @ApiOperation("获取所有可用的服务端")
    @GetMapping("/{name}/homepage")
    fun homepage(@PathVariable name: String)
     = database.homepages.find { it.id ilike name } ?: throw NotFound(
        errcode = 501,
        message = "server name `${name}` is not found."
    )
}
