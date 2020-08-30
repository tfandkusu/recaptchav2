package com.tfandkusu.recaptchav2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainViewModel : ViewModel() {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://recaptcha-dot-tfandkusu.appspot.com/")
        .build()

    private val service = retrofit.create(ApiService::class.java)

    /**
     * CAPTCHAの検証が成功した時のイベント
     */
    val success = MutableLiveData<Boolean>()


    /**
     * CAPTCHAの検証が失敗した時のイベント
     */
    val fail = MutableLiveData<String>()

    /**
     * プログレス表示
     */
    val progress = MutableLiveData(false)

    /**
     * コメントを投稿する
     * @param comment 投稿するコメント
     * @param token ユーザー応答トークン
     */
    fun postComment(comment: String, token: String) = viewModelScope.launch {
        try {
            progress.value = true
            service.postComment(CommentRequest(comment, token))
            success.value = true
        } catch (e: Throwable) {
            fail.value = e.toString()
        } finally {
            progress.value = false
        }
    }
}
