package com.github.fastmirrorserver.exception

class NotFound(errcode: String, message: String, details: String = "no further information"):
    ServiceException(404, errcode, message, details)