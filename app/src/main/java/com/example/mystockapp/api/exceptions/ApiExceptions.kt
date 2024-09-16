package com.example.mystockapp.api.exceptions

class ApiException(val code: Int, message: String?) : Exception(message)
class NetworkException(message: String, cause: Throwable) : Exception(message, cause)
class GeneralException(message: String, cause: Throwable) : Exception(message, cause)
