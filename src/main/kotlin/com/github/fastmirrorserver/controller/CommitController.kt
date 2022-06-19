package com.github.fastmirrorserver.controller

import com.github.fastmirrorserver.ErrorCodes
import com.github.fastmirrorserver.component.Cache
import com.github.fastmirrorserver.dto.Commit
import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.service.CommitService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v2")
class CommitController {
    @Autowired
    private lateinit var service: CommitService
    @Qualifier("memoryCacheImpl")
    @Autowired
    private lateinit var cache: Cache

    @PostMapping("/commit")
    fun postCommit(@RequestBody param: Commit.Request) = service.commit(param, false)

    @PutMapping("/commit")
    fun putCommit(@RequestBody param: Commit.Request) = service.commit(param, true)

    @PutMapping("/{name}/{version}/{coreVersion}/commit")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun uploadFile(
        @PathVariable name: String,
        @PathVariable version: String,
        @PathVariable coreVersion: String,
        file: MultipartFile
    ) {
        val commit = Commit(name = name, version = version, coreVersion = coreVersion)
        if(!cache.has(commit.digest))
            throw NotFound(
                errcode = ErrorCodes.Commit.prevent_modification,
                message = "Preventing Modification. It might be uploading timeout or not committing first."
            )
        service.uploadFile(commit, file.inputStream)
        cache.del(commit.digest)
    }
}