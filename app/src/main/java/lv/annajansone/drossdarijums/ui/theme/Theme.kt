package lv.annajansone.drossdarijums.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DrossDarijumsColors = darkColorScheme(
    primary = Color(0xFFD2CADF),
    onPrimary = Color(0xFF372937),
    background = Color(0xFF372937),
    onBackground = Color(0xFFF1E4DB),
    surface = Color(0xFF463646),
    onSurface = Color(0xFFF1E4DB),
    error = Color(0xFFB8404C),
    onError = Color.White,
)

@Composable
fun DrossDarijumsTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DrossDarijumsColors, content = content)
}

