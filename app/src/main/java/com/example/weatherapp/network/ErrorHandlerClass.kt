package com.example.weatherapp.network


import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null): ResultWrapper<Nothing>()
    object NetworkError: ResultWrapper<Nothing>()
}

    suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = convertErrorBody(throwable)
                        ResultWrapper.GenericError(code, errorResponse)
                    }

                    is SocketTimeoutException -> {
                        ResultWrapper.GenericError(
                            null,
                            ErrorResponse("", "Network Timeout", false)
                        )
                    }
                    else -> {
                        ResultWrapper.GenericError(null, null)
                    }
                }
            }
        }
    }


    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.string()?.let {
                val apiErrors = Gson().fromJson(it, ErrorResponse::class.java)
                apiErrors
            }
        } catch (exception: Exception) {
            null
        }
    }
