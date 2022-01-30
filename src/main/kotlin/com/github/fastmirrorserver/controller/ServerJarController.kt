package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.dto.History
import com.github.fastmirrorserver.dto.Latest
import com.github.fastmirrorserver.service.HistoryService
import com.github.fastmirrorserver.service.LatestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/serverjar")
class ServerJarController {
    @Autowired
    lateinit var latestService: LatestService
    @GetMapping("/latest")
    fun latest(param: Latest.Param): Map<String, Map<String, Latest.ResponseUnit>> {
        return latestService.query(param)
    }

    @Autowired
    lateinit var historyService: HistoryService
    @GetMapping("/history")
    fun history(param: History.Param): Map<String, Map<String, List<History.Response>>> {
        return historyService.query(param)
    }
}