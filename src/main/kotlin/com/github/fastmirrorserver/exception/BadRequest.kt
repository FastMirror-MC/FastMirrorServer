package com.github.fastmirrorserver.exception

class BadRequest(errcode: String, message: String, details: String = "no further information") :
    ServiceException(400, errcode, message, details)