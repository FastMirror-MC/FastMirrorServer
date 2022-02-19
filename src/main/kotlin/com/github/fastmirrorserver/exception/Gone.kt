package com.github.fastmirrorserver.exception

class Gone(errcode: Int, message: String, details: String = "no further information") :
    ServiceException(410, errcode, message, details)