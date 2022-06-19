package com.github.fastmirrorserver.exception

class Forbidden(errcode: String, message: String, details: String = "no further information") :
    ServiceException(403, errcode, message, details)