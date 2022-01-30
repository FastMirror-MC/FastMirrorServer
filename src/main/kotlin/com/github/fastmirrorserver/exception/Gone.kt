package com.github.fastmirrorserver.exception

class Gone(errcode: Int, message: String, details: String = "no further information", url: String) :
    ServiceException(410, errcode, message, details, url)