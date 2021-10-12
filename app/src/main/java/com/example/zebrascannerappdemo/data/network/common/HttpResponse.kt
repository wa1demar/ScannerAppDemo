package com.example.zebrascannerappdemo.data.network.common

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?
}
