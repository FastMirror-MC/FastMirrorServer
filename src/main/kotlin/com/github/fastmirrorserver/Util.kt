package com.github.fastmirrorserver

import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.entity.Homepages
import com.github.fastmirrorserver.entity.SubmitLogs
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val Database.cores get() = this.sequenceOf(Cores)
val Database.submits get() = this.sequenceOf(SubmitLogs)
val Database.projects get() = this.sequenceOf(Homepages)

private val UTC = ZoneId.of("UTC")

private fun getFileExt(name: String) = when(name.lowercase()) {
    "pocketmine" -> "phar"
    else -> "jar"
}

private val zoneId: ZoneId = ZoneId.systemDefault()
fun utc(dateTime: LocalDateTime) = dateTime.atZone(zoneId).format(DateTimeFormatter.ISO_INSTANT)!!
fun utc(string: String): LocalDateTime = ZonedDateTime.parse(string).withZoneSameInstant(UTC).toLocalDateTime()
fun getPath(name: String,version: String,coreVersion: String) =
    "$name/$version/$name-$version-$coreVersion.${getFileExt(name)}"