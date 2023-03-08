package com.github.fastmirrorserver.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val zone = ZoneId.systemDefault()
private val ZONE_UTC = ZoneId.of("UTC")
private val `ZONE_GMT+8` = ZoneId.of("Asia/Shanghai")

val LocalDateTime.UTC get() = this.atZone(zone).format(DateTimeFormatter.ISO_INSTANT)!!
val ZonedDateTime.UTC get() = format(DateTimeFormatter.ISO_INSTANT)!!
val String.UTC get() = ZonedDateTime.parse(this).withZoneSameInstant(ZONE_UTC)

val LocalDateTime.`GMT+8` get() = this.atZone(zone).withZoneSameInstant(`ZONE_GMT+8`).UTC
fun timestamp() = System.currentTimeMillis() / 1000