package com.github.fastmirrorserver.exception

class Gone(errcode: String, message: String, details: String = "no further information") :
    ServiceException(410, errcode, message, details)