package com.omelan.cofi.wearos.presentation.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import com.omelan.cofi.wearos.R

@Composable
fun StartFAB(isTimerRunning: Boolean, onClick: () -> Unit) {
    val transition = updateTransition(targetState = isTimerRunning, label = "Fab change")
    val animatedFabRadii by transition.animateFloat(label = "Fab radii") {
        if (it) 28.0f else 100f
    }
    val backgroundColor by transition.animateColor(label = "Fab color") {
        if (it) MaterialTheme.colorScheme.secondaryDim else MaterialTheme.colorScheme.primary
    }
    val iconColor by transition.animateColor(label = "Fab icon color") {
        if (it) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary
    }
    val size by transition.animateDp(label = "Fab size") {
        if (it) ButtonDefaults.CompactButtonHeight else ButtonDefaults.Height
    }
    val icon = remember(isTimerRunning) {
        if (isTimerRunning) R.drawable.ic_pause else R.drawable.ic_play
    }
    Button(
        modifier = Modifier
            .testTag("start_button")
            .size(size),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor, contentColor = iconColor),
        shape = RoundedCornerShape(animatedFabRadii),
    ) {
        Icon(painter = painterResource(icon), contentDescription = null)
    }
}
