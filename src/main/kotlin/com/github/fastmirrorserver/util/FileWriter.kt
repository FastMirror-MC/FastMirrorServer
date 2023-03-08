package com.github.fastmirrorserver.util

import com.github.fastmirrorserver.exception.ApiException
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import java.nio.channels.Channels
import java.nio.channels.FileChannel

class FileWriter(private val file: File, val size: Long) {
    private val records = ArrayList<Record>()
    private val tmp_file = File("./tmp/${file.name}.tmp")
    private val random_access_file: RandomAccessFile
    private val channel: FileChannel

    private var written_size = 0L
    
    private var released: Boolean = false

    init {
        if(tmp_file.exists()) tmp_file.delete()
        tmp_file.parentFile.mkdirs()
        random_access_file = RandomAccessFile(tmp_file, "rw")
        channel = random_access_file.channel
    }

    private fun recorded(start: Long, end: Long) {
        val idx = records.indexOfLast { it.end < start }

        val has_prev = idx != -1
        val has_next = idx + 1 < records.size
        val can_merged_with_prev = has_prev && records[idx].end + 1 == start
        val can_merged_with_next = has_next && records[idx + 1].start - 1 == end

        if(has_next && records[idx + 1].start <= end)
            throw ApiException.CONFLICT_UPLOAD

        if(can_merged_with_next && can_merged_with_prev) {
            records[idx].end = records[idx + 1].end
            records.removeAt(idx + 1)
        }
        else if(can_merged_with_prev) records[idx].end = end
        else if(can_merged_with_next) records[idx + 1].start = start
        else records.add(idx + 1, Record(start, end))
    }
    
    fun nextExpectedRange(maximum: Int = Int.MAX_VALUE): List<String> {
        val list = ArrayList<String>()
        if(records[0].start != 0L) list.add("0-${records[0].start - 1}")
        for(idx in 0 until (list.size - 1).coerceAtMost(maximum - 1))
            list.add("${records[idx].end + 1}-${records[idx + 1].start - 1}")
        if(records.size > 1 && list.size < maximum) records[records.size - 1].run { 
            if(end + 1 < size) list.add("${end + 1}-${size - 1}")
        }
        return list
    }
    
    fun write(stream: InputStream, position: Long, length: Long): Long {
        synchronized(this) { recorded(position, position + length - 1) }

        val read_chan = Channels.newChannel(stream)
        val written = channel.transferFrom(read_chan, position, length)
        read_chan.close()
        
        written_size += written
        return written
    }

//    fun write(data: ByteBuffer, start: Long, end: Long): Long {
//        if(data.position() != 0) data.flip()
//        if(data.limit() + start - 1  != end)
//            throw ApiException.UPLOAD_DATA_INCOMPLETE
//        
//        synchronized(this) { recorded(start, end) }
//
//        var written = 0L
//        while (data.hasRemaining()) {
//            written += channel.write(data, written + start)
//            data.compact()
//            data.flip()
//        }
//
//        written_size += written
//        return written
//    }

    fun flush(): Boolean {
        synchronized(this) {
            if(size > written_size) return false
            if(released) return false
            released = true
        }
        file.parentFile.mkdirs()
        val file_channel = file.outputStream().channel
        var written = 0L
        while(written < size) {
            written += file_channel.transferFrom(channel, written, size - written)
        }
        internalRelease()
        return true
    }

    fun release(): Boolean {
        internalRelease()
        return true
    }

    private fun internalRelease() {
        channel.close()
        random_access_file.close()
        tmp_file.delete()
    }

    private data class Record(
        var start: Long,
        var end: Long
    )
    
    class Builder(private val file: File) {
        private var size: Long = 0
        
        fun size(size: Long): Builder { this.size = size; return this }
        fun builder(): FileWriter = FileWriter(file, size)
    }
}
