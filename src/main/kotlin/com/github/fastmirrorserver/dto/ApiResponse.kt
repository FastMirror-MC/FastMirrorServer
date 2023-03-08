package com.github.fastmirrorserver.dto

data class ApiResponse(
    val data: Any?,
    val code: String,
    val success: Boolean,
    val message: String
) {
    companion object {
        fun success(data: Any? = null) = ApiResponse(
            data = data,
            code = "fin::success",
            success = true,
            message = "Request successfully."
        )
        fun failed(data: Any? = null) = ApiResponse(
            data = data,
            code = "err::unknown",
            success = false,
            message = "Unknown error."
        )
    }
}
