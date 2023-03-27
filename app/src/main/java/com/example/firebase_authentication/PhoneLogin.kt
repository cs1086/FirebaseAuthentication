package com.example.firebase_authentication

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

var verificationId = ""

@Composable
fun PhoneLogin() {
    val context = LocalContext.current

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            println("@@@@ onVerificationCompleted:$credential")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            println("@@@@ onVerificationFailed")
            if (e is FirebaseAuthInvalidCredentialsException) {

            } else if (e is FirebaseTooManyRequestsException) {

            }
            // Show a message and update the UI
        }

        override fun onCodeSent(
            verId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            verificationId = verId
            println("@@@@ onCodeSent:$verificationId")
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
            println("@@@@ onCodeAutoRetrievalTimeOut = $p0")
        }
    }

    Column {
        var text by remember {
            mutableStateOf("791125")
        }
        Button(onClick = {
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
//                .setPhoneNumber("+16505551234")
                .setPhoneNumber("+886 985109872")
                .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(context as Activity)
                .setCallbacks(callbacks) //
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }) {
            Text(text = "發送驗證")
        }
        TextField(value = text, onValueChange = { text = it })
        Button(onClick = {

            println("####verificationId=$verificationId,text=$text")
            val credential = PhoneAuthProvider.getCredential(verificationId, text)

            // 驗證驗證碼
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 驗證成功，進行下一步操作
                        println("驗證碼輸入正確!!!!")
                    } else {
                        // 驗證失敗，處理
                        println("驗證碼輸入錯誤....")
                    }
                }
        }) {
            Text(text = "確認驗證")
        }
    }
}