package com.example.alp_vp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.alp_vp.data.api.RetrofitClient
import com.example.alp_vp.data.local.TokenManager
import com.example.alp_vp.ui.route.AppNavigation
import com.example.alp_vp.ui.theme.ALPVPTheme
import com.example.alp_vp.ui.view.Friend.Friend
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize TokenManager
        val tokenManager = TokenManager(applicationContext)

        lifecycleScope.launch {
            // TEMP: token hasil login
            tokenManager.saveToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6OCwiZW1haWwiOiJib2JAdGVzdC5jb20iLCJpYXQiOjE3NjU5MzE5NDYsImV4cCI6MTc2NTkzNTU0Nn0.ao3WEFupt45M9ru6hPLwffSnbanHm-zU8eg68K6CRX8")

            RetrofitClient.initialize(tokenManager)

            setContent {
                ALPVPTheme {
                    Friend()
                }
        enableEdgeToEdge()
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                AppNavigation()
            }
        }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ALPVPTheme {
        Greeting("Android")
    }
}