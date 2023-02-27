package com.github.fastmirrorserver.pojos

import com.github.fastmirrorserver.utils.enums.Permission

data class AccountPOJO(
    val username: String,
    val password: String,
    val permission: Permission
)
