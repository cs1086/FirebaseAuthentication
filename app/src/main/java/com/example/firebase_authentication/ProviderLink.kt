package com.example.firebase_authentication

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun ProviderLink() {
    val context = LocalContext.current

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
                                val credential =
                                    EmailAuthProvider.getCredential("xtnact541@gmail.com", "abc123")
                                println("credential=$credential")

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
        Button(onClick = {
            startForResult.launch(googleSignInClient.signInIntent)

        }) {
            Text("email認證帳密連結給google帳號")
        }
    }
}