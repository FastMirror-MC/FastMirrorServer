package com.github.fastmirrorserver

import com.github.fastmirrorserver.entity.Cores
import com.github.fastmirrorserver.entity.Projects
import com.github.fastmirrorserver.entity.SubmitLogs
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

val Database.cores get() = this.sequenceOf(Cores)
val Database.submits get() = this.sequenceOf(SubmitLogs)
val Database.homepages get() = this.sequenceOf(Projects)

private val UTC = ZoneId.of("UTC")

private fun getFileExt(name: String) = when (name.lowercase()) {
    "pocketmine" -> "phar"
    else -> "jar"
}

private val zoneId: ZoneId = ZoneId.systemDefault()
fun utc(dateTime: LocalDateTime) = dateTime.atZone(zoneId).format(DateTimeFormatter.ISO_INSTANT)!!
fun utc(string: String): LocalDateTime = ZonedDateTime.parse(string).withZoneSameInstant(UTC).toLocalDateTime()
fun uuid() = UUID.randomUUID().toString().replace("-", "").uppercase()
fun getPath(name: String, version: String, coreVersion: String) =
    "$name/$version/$name-$version-$coreVersion.${getFileExt(name)}"
fun timestamp() = System.currentTimeMillis() / 1000