package com.example.gcsalesapp.api


sealed class Response<T>(val msg: String? = null, val code: Int = 0, val data: T? = null) {

    class Success<T>(msg: String, code: Int, data: T) : Response<T>(msg, code, data)
    class Error<T>(msg: String, code: Int) : Response<T>(msg, code)
//    class Loading<T>(data: T? = null) : Response<T>(data)

}