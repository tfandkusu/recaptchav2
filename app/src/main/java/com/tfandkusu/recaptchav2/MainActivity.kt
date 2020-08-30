package com.tfandkusu.recaptchav2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.success.observe(this) {
            if (it == true) {
                val adb = AlertDialog.Builder(this)
                adb.setMessage(R.string.success)
                adb.setPositiveButton(R.string.ok) { _, _ ->

                }
                adb.show()
            }
        }
        viewModel.fail.observe(this) { message ->
            message?.let {
                val adb = AlertDialog.Builder(this)
                adb.setTitle(R.string.error)
                adb.setMessage(it)
                adb.setPositiveButton(R.string.ok) { _, _ ->

                }
                adb.show()
            }
        }
        viewModel.progress.observe(this) { flag ->
            flag?.let {
                submit.isEnabled = !it
                progress.isVisible = it
            }
        }
        // 投稿ボタンをクリックしたときの処理
        submit.setOnClickListener {
            // 投稿コメント
            val comment = comment.text.toString()
            SafetyNet.getClient(this)
                .verifyWithRecaptcha(getString(R.string.api_site_key)/* サイトキーを入力 */)
                .addOnSuccessListener { tokenResponse ->
                    // 自動プログラムによる入力でないことの確認成功
                    tokenResponse.tokenResult?.let {
                        // 投稿コメントとユーザ応答トークンをサーバに送る
                        viewModel.postComment(comment, it)
                    }
                }
                .addOnFailureListener {
                    // 確認失敗
                    if (it is ApiException) {
                        viewModel.fail.value = CommonStatusCodes.getStatusCodeString(it.statusCode)
                    } else {
                        viewModel.fail.value = it.message
                    }
                }
        }
    }
}
