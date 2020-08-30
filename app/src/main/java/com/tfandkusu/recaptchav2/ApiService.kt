package com.tfandkusu.recaptchav2

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/comments")
    suspend fun postComment(@Body request: CommentRequest)
}
