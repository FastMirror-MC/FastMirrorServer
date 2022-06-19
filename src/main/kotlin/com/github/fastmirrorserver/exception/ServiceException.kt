package com.github.fastmirrorserver.exception

import com.github.fastmirrorserver.dto.ApiError

open class ServiceException(
    val entity: ApiError
    ): Exception() {
    constructor(
        status: Int,
        errcode: String,
        message: String,
        details: String,
        url: String = ""
    ) : this(
        ApiError(
            status = status,
            errcode = errcode,
            message = message,
            details = details,
            url = url
        )
    )
}