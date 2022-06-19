package com.github.fastmirrorserver.dto


data class Download (
    val name: String,
    val version: String,
    val coreVersion: String,
    val update: String,
    val artifact: String,
    val sha1: String,
    val url: String
)
