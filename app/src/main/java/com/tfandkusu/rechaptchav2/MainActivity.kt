package com.tfandkusu.rechaptchav2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private companion object {
        private const val TAG = "TryRechaptcha"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

// 投稿ボタンをクリックしたときの処理
submit.setOnClickListener {
    SafetyNet.getClient(this)
        .verifyWithRecaptcha(getString(R.string.api_site_key)/* サイトキーを入力 */)
        .addOnSuccessListener { tokenResponse ->
            // 自動プログラムによる入力でないことの確認成功
            tokenResponse.tokenResult?.let {
                Log.d(TAG, "Success: $it")
            }
        }
        .addOnFailureListener {
            // 確認失敗
            if (it is ApiException) {
                Log.d(TAG, "Error: ${CommonStatusCodes.getStatusCodeString(it.statusCode)}")
            } else {
                Log.d(TAG, "Error: ${it.message}")
            }
        }
}
    }
}
