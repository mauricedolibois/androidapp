// File: LockButton.kt
package pt.iade.games.iaderadio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import com.example.compose.outlineLight

@Composable
fun LockButton(
    isLocked: Boolean,
    onToggleLock: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    AppTheme {
        IconButton(
            onClick = { onToggleLock(!isLocked) },
            modifier = modifier
                .background(outlineLight, CircleShape)
                .size(100.dp)
        ) {
            Icon(
                imageVector = if (isLocked) Icons.Rounded.Lock else Icons.Rounded.LockOpen,
                contentDescription = if (isLocked) "Unlock" else "Lock",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }
    }
}
