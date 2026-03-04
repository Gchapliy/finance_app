package com.example.myapplication.core.ui.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TransparentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 34.dp,
    height: Dp = 34.dp,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit
) {
    var container by remember { mutableStateOf(Color.Transparent) }

    OutlinedButton(
        onClick = {
            container = Color.Gray.copy(alpha = 0.3f) // Temporary visual feedback
            onClick()
        },
        modifier = modifier
            .width(width)
            .height(height),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = container,
            contentColor = Color.Black,
        ),
        contentPadding = paddingValues,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}