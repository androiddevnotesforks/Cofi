package com.omelan.cofi.wearos.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material3.ConfirmationDialog
import androidx.wear.compose.material3.ConfirmationDialogDefaults
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.confirmationDialogCurvedText
import com.omelan.cofi.wearos.R

@Composable
fun OpenOnPhoneConfirm(isVisible: Boolean, onTimeout: () -> Unit) {
    val curvedTextStyle = ConfirmationDialogDefaults.curvedTextStyle
    val text = stringResource(R.string.common_open_on_phone)
    ConfirmationDialog(
        visible = isVisible,
        onDismissRequest = onTimeout,
        curvedText = {
            confirmationDialogCurvedText(
                text,
                curvedTextStyle,
            )
        },
    ) {
        Icon(
            painter = painterResource(id = R.drawable.common_full_open_on_phone),
            contentDescription = "",
        )
    }
}
