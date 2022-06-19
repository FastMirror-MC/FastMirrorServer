package com.github.fastmirrorserver.entity

import com.github.fastmirrorserver.sha1
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.varchar

interface User : Entity<User> {
    companion object: Entity.Factory<User>()

    var name: String
    var passwd: String
    var salt: String

    fun verify(password: String): Boolean = sha1("${password}:${salt}") == passwd
}

object Users : Table<User>("t_user") {
    val name = varchar("name").primaryKey()
    val passwd = varchar("authorization_string").bindTo { it.passwd }
    val salt = varchar("salt").bindTo { it.salt }
}