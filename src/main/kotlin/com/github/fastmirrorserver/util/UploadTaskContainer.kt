package com.github.fastmirrorserver.util

import com.github.fastmirrorserver.pojo.ManifestPOJO
import com.github.fastmirrorserver.dto.Metadata
import com.github.fastmirrorserver.exception.ApiException
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime
import java.util.TreeMap

class UploadTaskContainer {
    data class Task(
        val uri: String,
        private val builder: FileWriter.Builder,
        val expired: LocalDateTime
    ) {
        private lateinit var writer: FileWriter
        fun isExpired(now: LocalDateTime = LocalDateTime.now()) = expired < now
        
        fun write(stream: InputStream, position: Long, length: Long, total_size: Long): Long {
            if(!this::writer.isInitialized) writer = builder.size(total_size).builder()
            if(total_size != writer.size) throw ApiException.UPLOAD_FILE_SIZE_CHANGED
            return writer.write(stream, position, length)
        }
        fun nextExpectedRange(maximum: Int = Int.MAX_VALUE) = writer.nextExpectedRange(maximum)
        fun release() { if(this::writer.isInitialized) writer.release() }
        fun flush() = writer.flush()
    }
    private val task_pool: TreeMap<String, Task> = TreeMap()
    
    fun removeTask(tuple: Metadata) {
        task_pool[tuple.key]?.release()
        task_pool.remove(tuple.key)
    }
    
    fun createTask(pojo: ManifestPOJO, uri: String): Task? {
        if(has(pojo)) return null
        val task = Task(
            uri = uri,
            builder = FileWriter.Builder(File(pojo.filepath)),
            expired = LocalDateTime.now().plusMinutes(10)
        )
        task_pool[pojo.key] = task
        return task
    }
    
    fun getTask(tuple: Metadata): Task {
        val task = task_pool[tuple.key] ?: throw ApiException.TASK_NOT_FOUND
        if(!task.isExpired()) return task
        removeTask(tuple)
        throw ApiException.TASK_NOT_FOUND
    }
    
    fun closeTask(tuple: Metadata): Boolean {
        val status = getTask(tuple).flush()
        if(status)  removeTask(tuple)
        return status
    }
    
    fun has(tuple: Metadata): Boolean {
        val v = task_pool[tuple.key] ?: return false
        if(!v.isExpired()) return true
        removeTask(tuple)
        return false
    }
}
