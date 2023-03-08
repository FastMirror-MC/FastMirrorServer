package com.github.fastmirrorserver.exception

import com.github.fastmirrorserver.dto.ApiResponse
import org.springframework.http.HttpStatus

class ApiException(
    val status: HttpStatus,
    val code: String,
    message: String,
    val data: Any? = null
) : Throwable(message)
{
    fun toResponse() = ApiResponse(
        data = data,
        code =  code,
        success = false,
        message = message ?: "no further information."
    )
    fun withData(data: Any) = ApiException(
        status = status,
        code = code,
        message = message ?: "no further information",
        data = data
    )
    companion object {
        val AUTH_INVALID_FORMAT = ApiException(
            status = HttpStatus.BAD_REQUEST,
            code = "err::header::invalid_format",
            message = "Format of `Authorization` is invalid. Please use Basic HTTP Authorization."
        )
        val AUTH_METHOD_NOT_SUPPORTED = ApiException(
            status =  HttpStatus.BAD_REQUEST,
            code = "err::header::not_supported_method",
            message = "Method of `Authorization` is not supported. Please use Basic HTTP Authorization."
        )
        val CONTENT_RANGE_NOT_FOUND = ApiException(
            status = HttpStatus.BAD_REQUEST,
            code = "err::header::content-range_not_found",
            message = "Header `Content-Range` missed."
        )
        val CONTENT_RANGE_INVALID = ApiException(
            status = HttpStatus.BAD_REQUEST,
            code = "err::header::content-range_invalid",
            message = "Format of `Content-Range` is invalid."
        )
        val CONTENT_LENGTH_NOT_FOUND = ApiException(
            status = HttpStatus.BAD_REQUEST,
            code = "err::header::content-length_not_found",
            message = "Header `Content-Length` missed."
        )
        
        val ACCOUNT_USERNAME_INVALID = ApiException(
            status = HttpStatus.FORBIDDEN,
            code = "err::account::invalid_username",
            message = "Invalid username."
        )
        val ACCOUNT_PASSWORD_INVALID = ApiException(
            status = HttpStatus.FORBIDDEN,
            code = "err::account::invalid_password",
            message = "Invalid password."
        )
        val ACCOUNT_USERNAME_OR_PASSWORD_INVALID = ApiException(
            status = HttpStatus.FORBIDDEN,
            code = "err::account::invalid_username_password",
            message = "Username or password does not match the specification."
        )
        val PERMISSION_DENIED = ApiException(
            status = HttpStatus.UNAUTHORIZED,
            code = "err::permission::permission_denied",
            message = "Permission denied."
        )
        val REQUEST_LIMIT = ApiException(
            status = HttpStatus.FORBIDDEN,
            code = "err::permission::request_limit",
            message = "The maximum number of requests is reached."
        )
        
        val ARTIFACT_INFO_NOT_FOUND = ApiException(
            status = HttpStatus.NOT_FOUND,
            code = "err::info::not_found",
            message = "Enquiry not found."
        )
        val RESOURCE_NOT_FOUND = ApiException(
            status = HttpStatus.NOT_FOUND,
            code = "err::resource::not_found",
            message = "Requested resource does not exist."
        )
        val UPLOAD_FILE_SIZE_CHANGED = ApiException(
            status = HttpStatus.FORBIDDEN,
            code =  "err::upload::file_size_changed",
            message = "File size changed."
        )

        val UP_TO_DATE = ApiException(
            status = HttpStatus.FORBIDDEN,
            code = "err::upload::up_to_date",
            message = "Information is up-to-date."
        )
        val TASK_NOT_FOUND = ApiException(
            status = HttpStatus.NOT_FOUND,
            code = "err::upload::task_not_found",
            message = "Please create task first."
        )
        val CONFLICT_TASK = ApiException(
            status = HttpStatus.CONFLICT,
            code =  "err::upload::conflict_task",
            message = "This task is not finished."
        )
        val CONFLICT_UPLOAD = ApiException(
            status = HttpStatus.CONFLICT,
            code =  "err::upload::conflict_upload",
            message = "This chunk already submitted."
        )
        val UPLOAD_DATA_INCOMPLETE = ApiException(
            status = HttpStatus.FORBIDDEN,
            code = "err::upload::data_incomplete",
            message = "Mismatch between Content-Length and Range"
        )
        val TASK_NOT_FINISHED = ApiException(
            status = HttpStatus.FORBIDDEN,
            code =  "err::upload::task_not_finished",
            message = "This task is not finished."
        )
        val NEED_RETRANSMIT = ApiException(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            code = "err::upload::need_retransmit",
            message = "Unknown error. Please recreate task."
        )
    }
}