package com.github.fastmirrorserver.exception

class Forbidden(errcode: Int, message: String, details: String = "no further information") :
    ServiceException(403, errcode, message, details)