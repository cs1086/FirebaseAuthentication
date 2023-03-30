package com.example.firebase_authentication

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EmailPasswordLogin() {
    val context = LocalContext.current
    LaunchedEffect(Unit) {

        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                if (user.isEmailVerified) {
                    println("signInWithEmail:addAuthStateListener.success")

                    // 用戶已驗證其電子郵件地址
                } else {
                    println("signInWithEmail:addAuthStateListener.fail")

                    // 用戶尚未驗證其電子郵件地址
                }
            } else {
                // 用戶已登出 Firebase
            }
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword("xtnact541@gmail.com", "abc123")
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    println("signInWithEmail:success")
                    val user = FirebaseAuth.getInstance().currentUser
                    println("user1.isEmailVerified=${user?.isEmailVerified}")
                    FirebaseAuth.getInstance().signOut()
                } else {
                    println("signInWithEmail:failure")
                }
            }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword("xtnact541@gmail.com", "abc123")
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.sendEmailVerification()//發送驗證碼信件
                        ?.addOnCompleteListener { sendEmailTask ->
                            if (sendEmailTask.isSuccessful) {
                                println("驗證電子郵件已發送")
                                // 驗證電子郵件已成功發送，請通知用戶檢查其收件箱
                                Toast.makeText(context, "驗證電子郵件已發送", Toast.LENGTH_SHORT).show()
                            } else {
                                println("發送驗證電子郵件時出錯")
                                // 發送驗證電子郵件時出錯，請告知用戶重試
                                Toast.makeText(context, "發送驗證電子郵件時出錯", Toast.LENGTH_SHORT).show()
                            }
                        }
                    println("email user = ${FirebaseAuth.getInstance().currentUser}")
                } else {

                    println("firebase創建失敗")
                }
            }.addOnFailureListener {
                println("firebase創建失敗原因=${it}")

            }
    }
}