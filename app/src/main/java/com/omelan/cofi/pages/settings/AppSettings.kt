@file:OptIn(ExperimentalMaterial3Api::class)

package com.omelan.cofi.pages.settings

import androidx.activity.compose.LocalActivity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.omelan.cofi.R
import com.omelan.cofi.components.PiPAwareAppBar
import com.omelan.cofi.components.createAppBarBehavior
import com.omelan.cofi.utils.WearUtils
import com.omelan.cofi.utils.getDefaultPadding

data class AppSetting(
    @StringRes val title: Int,
    @DrawableRes val icon: Int? = null,
    val imageVector: ImageVector? = null,
    val onClick: () -> Unit,
)

@Composable
fun AppSettings(
    goBack: () -> Unit,
    goToAbout: () -> Unit,
    goToTimerSettings: () -> Unit,
    goToBackupRestore: () -> Unit,
    gotToAppearance: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val appBarBehavior = createAppBarBehavior()
    val activity = LocalActivity.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var wearNodesWithoutApp by remember {
        mutableStateOf(listOf<String>())
    }
    WearUtils.ObserveIfWearAppInstalled {
        wearNodesWithoutApp = it
    }

    val settingsList = listOf(
        AppSetting(
            title = R.string.settings_timer_item,
            icon = R.drawable.ic_timer,
            onClick = goToTimerSettings ,
        ),
        AppSetting(
            title = R.string.settings_backup_item,
            icon = R.drawable.ic_save,
            onClick = goToBackupRestore ,
        ),
        AppSetting(
            title = R.string.settings_appearance_item,
            imageVector = Icons.Rounded.Face,
            onClick = gotToAppearance ,
        ),
        AppSetting(
            title = R.string.settings_about_item,
            imageVector = Icons.Rounded.Info,
            onClick = goToAbout ,
        ),
        if (activity != null && wearNodesWithoutApp.isNotEmpty()) AppSetting(
            title = R.string.settings_wearOS_item,
            icon = R.drawable.ic_watch,
            onClick = {
                WearUtils.openPlayStoreOnWearDevicesWithoutApp(
                    lifecycleOwner,
                    activity,
                    wearNodesWithoutApp,
                )
            },
        ) else null,
        AppSetting(
            title = R.string.settings_bug_item,
            icon = R.drawable.ic_bug_report,
            onClick = {
                uriHandler.openUri("https://github.com/rozPierog/Cofi/issues")
            },
        ),
    )


    Scaffold(
        topBar = {
            PiPAwareAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = appBarBehavior,
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .nestedScroll(appBarBehavior.nestedScrollConnection)
                .fillMaxSize(),
            contentPadding = getDefaultPadding(
                paddingValues = it,
                additionalStartPadding = 0.dp,
                additionalEndPadding = 0.dp,
            ),
        ) {
            itemsIndexed(
                items = settingsList.filterNotNull(),
                key = { i, item -> item.title },
            ) { index, setting ->
                ListItem(
                    headlineContent = {
                        Text(text = stringResource(id = setting.title))
                    },
                    leadingContent = {
                        if (setting.icon != null) {
                            Icon(
                                painter = painterResource(id = setting.icon),
                                contentDescription = null,
                            )
                        } else if (setting.imageVector != null) {
                            Icon(
                                imageVector = setting.imageVector,
                                contentDescription = null,
                            )
                        }
                    },
                    modifier = Modifier.settingsItemModifier(onClick = setting.onClick),
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun SettingsPagePreview() {
    AppSettings(
        goBack = { },
        goToAbout = { },
        goToTimerSettings = {},
        goToBackupRestore = {},
        gotToAppearance = {},
    )
}
