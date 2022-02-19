package com.github.fastmirrorserver.exception

class Unauthorized(errcode: Int, message: String, details: String = "no further information") :
    ServiceException(401, errcode, message, details)