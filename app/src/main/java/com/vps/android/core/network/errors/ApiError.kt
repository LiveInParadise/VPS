package com.vps.android.core.network.errors

import java.io.IOException

sealed class RequestError(override val message: String) : IOException(message) {
    class BadRequest(message: String?) : RequestError(message ?: "Bad Request")
    class Unauthorized(message: String?) : RequestError(message ?: "Authorization token required")
    class Forbidden(message: String?) : RequestError(message ?: "Access denied")
    class NotFound(message: String?) : RequestError(message ?: "Requested page not found")
    class InternalServerError(message: String?) : RequestError(message ?: "Internal server error")
    class UnknownError(message: String?) : RequestError(message ?: "Unknown error")
}

sealed class ApiError(override val message: String) : IOException(message) {
    class GeneralError(message: String?) : ApiError(message ?: "API Error")
    class NoDataError(message: String?) : ApiError(message ?: "No data error")
    class BreakDataError(message: String?) : ApiError(message ?: "Break data error")
    class TokenError(message: String?) : ApiError(message ?: "Authorization token error")
    class UnknownError(message: String?) : ApiError(message ?: "Unknown error")
}

class ErrorBody(
    val error: String? = null
)
