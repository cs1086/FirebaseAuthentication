package com.example.firebase_authentication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firebase_authentication.ui.theme.FirebaseAuthenticationTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseAuthenticationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String) {
//    FacebookLogin()//Facebook登入
//    GoogleLogin()//Google登入
//    EmailPasswordLogin()//Email密碼登入
//    PhoneLogin()//電話登入
//    ProviderLink()//多重帳號綁定
    ProviderLinkByPhone()//電話綁定帳號
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FirebaseAuthenticationTheme {
        Greeting("Android")
    }
}