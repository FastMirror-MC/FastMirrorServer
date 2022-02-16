package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.entity.Cores
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@Api("杂项", description = "核心名称，游戏版本等")
@RestController
class UtilController {
    @Autowired
    lateinit var database: Database

    @ApiOperation("获取所有可用的服务端")
    @GetMapping("/names")
    fun nameList(): List<String> {
        return database.from(Cores).selectDistinct(Cores.name).map { it[Cores.name] }.filterNotNull()
    }

    @ApiOperation("获取所有核心可用的游戏版本")
    @GetMapping("/versions")
    fun versionsList(): Map<String, List<String>> {
        return database.from(Cores).selectDistinct(Cores.name, Cores.version)
            .map { it[Cores.name]!! to it[Cores.version]!! }
            .groupBy { it.first }
            .mapValues { entry -> entry.value.map { it.second } }
    }

    @ApiOperation("获取指定核心可用的游戏版本")
    @GetMapping("/versions/{name}")
    fun versionList(@PathVariable name: String): List<String> {
        return database.from(Cores).selectDistinct(Cores.version)
            .where { Cores.name eq name }
            .map { it[Cores.version]!! }
    }
}
