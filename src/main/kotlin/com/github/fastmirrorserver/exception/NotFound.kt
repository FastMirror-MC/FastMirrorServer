package com.github.fastmirrorserver.exception

class NotFound(errcode: Int, message: String, details: String = "no further information"):
    ServiceException(404, errcode, message, details)