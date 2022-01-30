package com.github.fastmirrorserver.enums

import java.util.*

private val maps = TreeMap<String, McVersion>()

enum class McVersion(private val id: String) {
    V1_18_1("1.18.1"),
    V1_18("1.18"),
    General("general"),
    V1_19("1.19"),
    ;

    init { maps[id.lowercase()] = this }
    companion object {
        fun get(name: String): McVersion { return maps[name.lowercase()]!! }
    }

    override fun toString(): String { return this.id }
}