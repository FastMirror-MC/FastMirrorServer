package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.cores
import com.github.fastmirrorserver.dto.FileToken
import com.github.fastmirrorserver.dto.Submit
import com.github.fastmirrorserver.exception.UnauthorizedException
import com.github.fastmirrorserver.submits
import org.ktorm.database.Database
import org.ktorm.entity.add
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths

@Service
class SubmitService {
    val log = LoggerFactory.getLogger(SubmitService::class.java)
    @Autowired
    protected lateinit var database: Database
//    @Value("\${token-file-path}")
    val tokenFilePath: String = Paths.get("token").toAbsolutePath().toString()

    fun verify(token: String) {
        val localToken = File(tokenFilePath).readText().trim()
        if(token != localToken)
            throw UnauthorizedException()
    }

    fun query(param: Submit): FileToken {
        verify(param.token)
        val entity = param.toEntity()
        database.cores.add(entity)
        database.submits.add(param.toSubmitLog())
        return FileToken(
            path = entity.path,
            sha1 = entity.sha1
        )
    }
}
