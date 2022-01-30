package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.enums.CoreType
import com.github.fastmirrorserver.enums.McVersion
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UtilController {
    @GetMapping("/names")
    fun nameList(): List<String> {
        return CoreType.values().map { it.toString() }
    }

    @GetMapping("/versions")
    fun versionList(): List<String> {
        return McVersion.values().map { it.toString() }
    }
}