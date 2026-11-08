package lv.annajansone.drossdarijums

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import lv.annajansone.drossdarijums.ui.DrossDarijumsApp
import lv.annajansone.drossdarijums.ui.DrossDarijumsViewModel
import lv.annajansone.drossdarijums.ui.theme.DrossDarijumsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrossDarijumsTheme {
                val viewModel: DrossDarijumsViewModel = viewModel()
                DrossDarijumsApp(viewModel)
            }
        }
    }
}

