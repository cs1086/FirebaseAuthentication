package com.example.firebase_authentication

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun ProviderLinkByPhone() {
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

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("565215802676-v2nqih34fck6198ddp09n7m2hucbd48j.apps.googleusercontent.com")
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(LocalContext.current as Activity, gso)

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            println("result.resultCode = ${result.resultCode}")
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    println("####account = ${account.email},idToken = ${account.idToken}")

                    val credential =
                        GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task2: Task<AuthResult?> ->
                            if (task2.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                println("user = ${user?.email}")
                                println("####身份验证成功")

                            } else {
                                println("####身份验证失败")
                                val exception = task2.exception
                                println("signInWithCredential:failure=$exception")
                                // 身份验证失败
                                // 处理错误...
                            }
                        }
                }
            }
        }
    Column() {
        var text by remember {
            mutableStateOf("791125")
        }
        Button(onClick = {
            startForResult.launch(googleSignInClient.signInIntent)

        }) {
            Text("google登入")
        }
        Button(onClick = {
            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
//                .setPhoneNumber("+16505551234")
                .setPhoneNumber("+886985109872")
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
            val credential = PhoneAuthProvider.getCredential(verificationId, text)

            // 驗證驗證碼
            FirebaseAuth.getInstance().currentUser!!.linkWithCredential(
                credential
            )
                .addOnCompleteListener(context as Activity) { task ->
                    if (task.isSuccessful) {
                        println("linkWithCredential:success")
                        val user = task.result?.user
                    } else {
                        println("linkWithCredential:failure")
                    }
                }.addOnFailureListener { println("addOnFailureListener:$it") }

        }) {
            Text("綁定新的電話")
        }
    }
}