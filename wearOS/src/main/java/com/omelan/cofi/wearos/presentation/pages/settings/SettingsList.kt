@file:OptIn(ExperimentalHorologistApi::class)

package com.omelan.cofi.wearos.presentation.pages.settings

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.foundation.rotary.rotaryScrollable
import androidx.wear.compose.material3.*
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.omelan.cofi.share.*
import com.omelan.cofi.share.R
import com.omelan.cofi.wearos.presentation.components.OpenOnPhoneConfirm
import com.omelan.cofi.wearos.presentation.model.DataStore
import com.omelan.cofi.wearos.presentation.model.SYNC_SETTINGS_FROM_PHONE_DEFAULT_VALUE
import com.omelan.cofi.wearos.presentation.utils.WearUtils
import kotlinx.coroutines.launch


@Composable
fun Settings(navigateToLicenses: () -> Unit) {
    val activity = LocalActivity.current as ComponentActivity
    val dataStore = DataStore(activity)
    val lazyListState = rememberScalingLazyListState()
    val focusRequester = remember { FocusRequester() }

    val coroutineScope = rememberCoroutineScope()
    val getSettingsFromPhone by dataStore.getSyncSettingsFromPhoneSetting()
        .collectAsState(initial = SYNC_SETTINGS_FROM_PHONE_DEFAULT_VALUE)
    val stepChangeSound by dataStore.getStepChangeSoundSetting()
        .collectAsState(initial = STEP_SOUND_DEFAULT_VALUE)
    val stepChangeVibration by dataStore.getStepChangeVibrationSetting()
        .collectAsState(initial = STEP_VIBRATION_DEFAULT_VALUE)
    val weightSettings by dataStore.getWeightSetting()
        .collectAsState(initial = COMBINE_WEIGHT_DEFAULT_VALUE)
//    val backgroundTimer by dataStore.getBackgroundTimerSetting()
//        .collectAsState(initial = false)
    var showConfirmation by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val versionName = remember {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    }
    ScreenScaffold(
        scrollState = lazyListState,
        scrollIndicator = {
            ScrollIndicator(lazyListState)
        },
    ) {
        ScalingLazyColumn(
            state = lazyListState,
            modifier = Modifier
                .rotaryScrollable(
                    behavior = RotaryScrollableDefaults.behavior(lazyListState),
                    focusRequester = focusRequester,
                ),
        ) {
            item {
                Text(text = stringResource(id = R.string.settings_title))
            }
//            item {
//                ToggleChip(
//                    checked = backgroundTimer ?: false,
//                    onCheckedChange = {
//                        coroutineScope.launch {
//                            dataStore.setBackgroundTimerEnabled(it)
//                            context.askForNotificationPermission()
//                        }
//                    },
//                    label = {
//                        Text(text = stringResource(id = R.string.settings_background_timer_item))
//                    },
//                    toggleControl = {
//                        Switch(
//                            checked = backgroundTimer ?: false,
//                            onCheckedChange = {
//                                coroutineScope.launch {
//                                    dataStore.setBackgroundTimerEnabled(it)
//                                    context.askForNotificationPermission()
//                                }
//                            },
//                        )
//                    },
//                )
//            }
            item {
                SwitchButton(
                    label = {
                        Text(
                            text = stringResource(id = R.string.settings_sync_with_phone),
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    checked = getSettingsFromPhone,
                    onCheckedChange = {
                        coroutineScope.launch {
                            dataStore.setSyncSettingsFromPhone(it)
                        }
                    },
                    enabled = true,
                )
            }
            item {
                SwitchButton(
                    label = {
                        Text(
                            text = stringResource(id = R.string.settings_step_sound_item),
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    checked = stepChangeSound,
                    enabled = !getSettingsFromPhone,
                    onCheckedChange = {
                        coroutineScope.launch {
                            dataStore.setStepChangeSound(it)
                        }
                    },
                )
            }
            item {
                SwitchButton(
                    checked = stepChangeVibration,
                    enabled = !getSettingsFromPhone,
                    onCheckedChange = {
                        coroutineScope.launch {
                            dataStore.setStepChangeVibration(it)
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.settings_step_vibrate_item),
                            overflow = TextOverflow.Ellipsis,
                        )

                    },
                )
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        AnimatedContent(stringToCombineWeight(weightSettings).settingsStringId) {
                            Text(text = stringResource(it),)
                        }
                    },
                    enabled = !getSettingsFromPhone,
                    onClick = {
                        val values = CombineWeight.entries.toTypedArray()
                        coroutineScope.launch {
                            dataStore.selectCombineMethod(
                                values.getOrElse(
                                    values.indexOfFirst { it.name == weightSettings } + 1,
                                ) { values.first() },
                            )
                        }
                    },
                )
            }
            item {
                Text(text = stringResource(id = R.string.step_type_other))
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = navigateToLicenses,
                    label = {
                        Text(text = stringResource(id = R.string.settings_licenses_item))
                    },
                )
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        WearUtils.openLinkOnPhone(
                            "https://github.com/rozPierog/Cofi/blob/main/docs/Changelog.md",
                            activity = activity,
                            onSuccess = { showConfirmation = true },
                        )
                    },
                    label = {
                        Text(text = stringResource(id = R.string.app_version))
                    },
                    secondaryLabel = {
                        Text(
                            text = versionName ?: "Unknown",
                            fontWeight = FontWeight.Light,
                        )
                    },
                )
            }
        }
        OpenOnPhoneConfirm(isVisible = showConfirmation, onTimeout = { showConfirmation = false })
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
fun SettingsPreview() {

}
