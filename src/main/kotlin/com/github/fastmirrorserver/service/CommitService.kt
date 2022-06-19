package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.ErrorCodes
import com.github.fastmirrorserver.component.Cache
import com.github.fastmirrorserver.cores
import com.github.fastmirrorserver.dto.Commit
import com.github.fastmirrorserver.exception.NotFound
import com.github.fastmirrorserver.submits
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find
import org.ktorm.entity.update
import org.postgresql.util.PSQLException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Service
class CommitService {
    private val logger = LoggerFactory.getLogger(CommitService::class.java)
    @Autowired
    private lateinit var database: Database

    @Qualifier("memoryCacheImpl")
    @Autowired
    private lateinit var cache: Cache

    fun commit(param: Commit.Request, override: Boolean): Commit.Response {
        if(override) database.cores.update(param.toEntity())
        else try{
            database.cores.add(param.toEntity())
        }catch(e: PSQLException){
            logger.warn(e.message ?: e::class.java.name)
        }

        database.submits.add(param.toCommitLog())
        cache.add(param.digest, "enable", 60 * 5)
        return param.toResponse(60 * 5)
    }

    fun uploadFile(param: Commit, stream: InputStream) {
        val path = File(database.cores.find {
            (it.name eq param.name) and (it.version eq param.version) and (it.coreVersion eq param.coreVersion)
        }?.path ?: throw NotFound(
            errcode = ErrorCodes.core_info_not_found,
            message = "Commit core info (e.g. access /api/v2/commit) first."
        ))
        path.parentFile.mkdirs()
        val fp = FileOutputStream(path)
        fp.write(stream.readAllBytes())
        fp.close()
    }
}
