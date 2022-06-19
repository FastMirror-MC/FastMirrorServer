package com.github.fastmirrorserver.exception

class Unauthorized(errcode: String, message: String, details: String = "no further information") :
    ServiceException(401, errcode, message, details)