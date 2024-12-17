package com.autonomy_lab.fetchexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.autonomy_lab.fetchexercise.ui.main.MainScreen
import com.autonomy_lab.fetchexercise.ui.theme.FetchExerciseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchExerciseTheme {
                MainScreen()
            }
        }
    }
}
