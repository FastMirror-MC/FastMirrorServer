package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.service.DetailsService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Api("服务端核心信息获取", description = "服务端核心信息获取")
@RestController
@RequestMapping("/api/v2")
class DetailsController {
    @Autowired
    lateinit var service: DetailsService

    @ApiOperation("获取支持的服务端列表")
    @GetMapping("")
    fun summary() = service.summary()

    @ApiOperation("获取所有服务端支持的游戏列表")
    @GetMapping("/all")
    fun versions() = service.versions()

    @ApiOperation("获取服务端支持的游戏版本列表")
    @ApiImplicitParams(ApiImplicitParam(value = "服务端名称", name = "name", required = true, example = "Arclight"))
    @GetMapping("/{name}")
    fun versions(@PathVariable name: String) = service.versions(name)

    @ApiOperation("获取获取对应游戏版本的构建信息列表")
    @ApiImplicitParams(
        ApiImplicitParam(value = "服务端名称", name = "name", required = true, example = "Arclight"),
        ApiImplicitParam(value = "游戏版本", name = "version", required = true, example = "1.15"),
        ApiImplicitParam(value = "分页起始索引", name = "offset", required = false, example = "0", defaultValue = "0"),
        ApiImplicitParam(value = "分页长度", name = "limit", required = false, example = "25", defaultValue = "25")
    )
    @GetMapping("/{name}/{version}")
    fun coreVersions(
        @PathVariable name: String, @PathVariable version: String,
        @RequestParam("offset", required = false) offset: Int?,
        @RequestParam("limit", required = false) limit: Int?
    ) = service.coreVersion(name, version, offset ?: 0, limit ?: 25)

    @ApiOperation("获取指定核心信息")
    @ApiImplicitParams(
        ApiImplicitParam(value = "服务端名称", name = "name", required = true, example = "Arclight"),
        ApiImplicitParam(value = "游戏版本", name = "version", required = true, example = "1.15"),
        ApiImplicitParam(value = "核心版本", name = "coreVersion", required = true, example = "latest")
    )
    @GetMapping("/{name}/{version}/{coreVersion}")
    fun artifact(
        @PathVariable name: String, @PathVariable version: String, @PathVariable coreVersion: String
    ) = try {
        service.artifact(name, version, coreVersion)
    } catch (e: NullPointerException) {
        throw NotFound(
            errcode = 101,
            message = "resource not found"
        )
    }

    @ApiOperation("获取核心下载所需信息")
    @ApiImplicitParams(
        ApiImplicitParam(value = "服务端名称", name = "name", required = true, example = "Arclight"),
        ApiImplicitParam(value = "游戏版本", name = "version", required = true, example = "1.15"),
        ApiImplicitParam(value = "核心版本", name = "coreVersion", required = true, example = "latest")
    )
    @GetMapping("/{name}/{version}/{coreVersion}/download")
    fun download(
        @PathVariable name: String, @PathVariable version: String, @PathVariable coreVersion: String
    ) = service.download(name, version, coreVersion)
}
