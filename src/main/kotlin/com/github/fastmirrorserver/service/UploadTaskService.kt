package com.github.fastmirrorserver.service

import com.github.fastmirrorserver.pojo.ManifestPOJO
import com.github.fastmirrorserver.dto.Metadata
import com.github.fastmirrorserver.entity.Manifest
import com.github.fastmirrorserver.entity.Manifests
import com.github.fastmirrorserver.exception.ApiException
import com.github.fastmirrorserver.util.*
import org.ktorm.database.Database
import org.ktorm.entity.firstOrNull
import org.ktorm.support.postgresql.insertOrUpdate
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class UploadTaskService {
    private val logger = LoggerFactory.getLogger(this::class.java)
    @Autowired
    private lateinit var database: Database
    
    private val tasks = UploadTaskContainer()
    
    private fun insertOrUpdate(pojo: ManifestPOJO) = database.insertOrUpdate(Manifests) {
        set(it.name,         pojo.name)
        set(it.mc_version,   pojo.mc_version)
        set(it.core_version, pojo.core_version)
        set(it.update_time,  pojo.update_time)
        set(it.sha1,         pojo.sha1)
        set(it.filename,     pojo.filename)
        set(it.path,         pojo.filepath)
        set(it.enable, false)
        onConflict {
            set(it.update_time,  pojo.update_time)
            set(it.sha1,         pojo.sha1)
            set(it.filename,     pojo.filename)
            set(it.path,         pojo.filepath)
            set(it.enable, false)
        }
    }
    
    private fun isEquivalent(pojo: ManifestPOJO, manifest: Manifest)
        = pojo.update_time == manifest.update_time
            && pojo.sha1 == manifest.sha1
    
    fun createTask(pojo: ManifestPOJO, request: HttpServletRequest): Map<String, String?> {
        val entity = database.all_cores
            .querySpecificArtifact(pojo)
            .firstOrNull()
                                                //   有记录  任务列表无  已启用  数据有更新
        if(entity == null)                      //     否        --        --        --     创建任务
            insertOrUpdate(pojo)
        else if(tasks.has(pojo))                //     是        否        --        --     抛异常
//            throw ApiException.CONFLICT_TASK
            tasks.removeTask(pojo)
        else if(!entity.enable)                 //     是        是        否        --     创建任务
            insertOrUpdate(pojo)
        else if(isEquivalent(pojo, entity))     //     是        是        是        否     抛异常
            throw ApiException.UP_TO_DATE
        else if(entity.sha1 == pojo.sha1 && entity.enable) {
            entity.update_time = pojo.update_time
            entity.flushChanges()
            return mapOf(
                "upload_uri" to null,
                "expiration_time" to null,
                "api_type" to "fastmirror.v3"
            )
        }
        else insertOrUpdate(pojo)               //     是        是        是        是     创建任务
        
        val task = tasks.createTask(pojo, pojo.uploadUrl(request)) ?: throw ApiException.CONFLICT_TASK
        
        return mapOf(
            "upload_uri" to task.uri,
            "expiration_time" to task.expired.UTC,
            "api_type" to "fastmirror.v3"
        )
    }
    
    private val range_regex = Regex("""^bytes[ =](?<start>\d+)?-(?<end>\d+)?/(?<size>\d+)$""")
    fun uploadFile(name: String, mc_version: String, core_version: String, request: HttpServletRequest): Map<String, Any> {
        val tuple = Metadata(name, mc_version, core_version)
        
        val length = request.getIntHeader("Content-Length").let {
            if(it > 0) it else throw ApiException.CONTENT_LENGTH_NOT_FOUND
        }.toLong()
        val (start, end, size) = request.run {
            val range = getHeader("Content-Range") ?: throw ApiException.CONTENT_RANGE_NOT_FOUND
            val match = range_regex.find(range.trim())?.groups ?: throw ApiException.CONTENT_RANGE_INVALID
            val size = match["size"]?.value?.toLong() ?: throw ApiException.CONTENT_RANGE_INVALID
            val start = match["start"]?.value?.toLong() ?: 0
            val end = match["end"]?.value?.toLong() ?: size
            Triple(start, end, size)
        }
        
        if(start < 0 || start >= end || end > size || start + length - 1 != end)
            throw ApiException.CONTENT_RANGE_INVALID

        val task = tasks.getTask(tuple)
        val stream = request.inputStream

        try { task.write(stream, start, length, size) }
        catch (e: ApiException) {
            throw e.withData(mapOf(
                "next_expected_range" to task.nextExpectedRange(),
                "expired_time" to task.expired.`GMT+8`
            ))
        }
        
        return mapOf(
            "next_expected_range" to task.nextExpectedRange(2),
            "expired_time" to task.expired.`GMT+8`
        )
    }
    
    fun closeTask(name: String, mc_version: String, core_version: String) {
        val tuple = Metadata(name, mc_version, core_version)
        val status = tasks.closeTask(tuple)
        
        if(!status) throw ApiException.TASK_NOT_FINISHED
        
        val core = database.all_cores.querySpecificArtifact(tuple).firstOrNull() ?: throw ApiException.NEED_RETRANSMIT
        core.enable = true
        core.flushChanges()
        logger.info("info flushed.")
    }
}
