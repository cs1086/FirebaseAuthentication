package com.example.firebase_authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FacebookLogin() {
    val fbCallbackManager = CallbackManager.Factory.create()
    val buttonFacebookLogin = LoginButton(LocalContext.current)
    buttonFacebookLogin.setPermissions(listOf("email"))
    buttonFacebookLogin.registerCallback(fbCallbackManager, object : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            val credential = FacebookAuthProvider.getCredential(loginResult.accessToken.token)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                    } else {

                    }
                }.addOnFailureListener {

                }
        }

        override fun onCancel() {
            println("facebook:onCancel")

        }

        override fun onError(error: FacebookException) {
            println("facebook:onError")
        }
    })
    Column() {
        Button(onClick = {
            buttonFacebookLogin.performClick()
        }) {

        }
    }
}