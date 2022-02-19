package com.github.fastmirrorserver.exception

class BadRequest(errcode: Int, message: String, details: String = "no further information") :
    ServiceException(400, errcode, message, details)