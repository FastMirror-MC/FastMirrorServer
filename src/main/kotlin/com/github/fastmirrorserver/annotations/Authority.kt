package com.github.fastmirrorserver.annotations

import com.github.fastmirrorserver.utils.enums.Permission

annotation class Authority(val permission: Permission = Permission.NONE, val ignore_in_debug:Boolean = true)