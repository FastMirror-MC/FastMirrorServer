package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.cores
import com.github.fastmirrorserver.dto.FileToken
import com.github.fastmirrorserver.dto.Submit
import com.github.fastmirrorserver.exception.Unauthorized
import com.github.fastmirrorserver.exception.UnauthorizedException
import com.github.fastmirrorserver.submits
import org.ktorm.entity.add
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class SubmitService: QueryService<Submit, FileToken>() {
//    @Value("\${token-file-path}")
    lateinit var tokenFilePath: String

    fun verify(token: String) {
        if(!::tokenFilePath.isInitialized) tokenFilePath = "./token"
        if(token != File(tokenFilePath).readText())
            throw UnauthorizedException()
    }

    override fun query(param: Submit): FileToken {
        verify(param.token)
        val entity = param.toEntity()
        database.cores.add(entity)
        database.submits.add(param.toSubmitLog())
        return FileToken(
            name = entity.name,
            version = entity.version,
            build = entity.build,
            sha1 = entity.sha1
        )
    }
}
