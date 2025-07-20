package com.omelan.cofi.wearos.presentation.pages.details

import android.view.KeyEvent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.*
import com.google.android.horologist.compose.ambient.AmbientAware
import com.google.android.horologist.compose.layout.fillMaxRectangle
import com.omelan.cofi.share.R
import com.omelan.cofi.share.components.TimeText
import com.omelan.cofi.share.components.TimerValue
import com.omelan.cofi.share.model.Recipe
import com.omelan.cofi.share.timer.TimerControllers
import com.omelan.cofi.share.utils.toStringShort
import com.omelan.cofi.wearos.presentation.components.ListenKeyEvents
import com.omelan.cofi.wearos.presentation.components.StartFAB
import kotlinx.coroutines.launch

@Composable
fun TimerPage(
    timerControllers: TimerControllers,
    recipe: Recipe,
    weightMultiplier: Float,
    timeMultiplier: Float,
) {

    val (
        animationControllers,
        currentStep,
        _,
        _,
        changeToNextStep,
        isDone,
        isTimerRunning,
        alreadyDoneWeight,
    ) = timerControllers
    val (
        animatedProgressValue,
        animatedProgressColor,
        pauseAnimations,
        _,
        resumeAnimations,
    ) = animationControllers
    var showDescriptionDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val startButtonOnClick: () -> Unit = {
        if (currentStep != null) {
            if (animatedProgressValue.isRunning) {
                coroutineScope.launch { pauseAnimations() }
            } else {
                coroutineScope.launch {
                    if (currentStep.time == null) {
                        changeToNextStep(false)
                    } else {
                        resumeAnimations()
                    }
                }
            }
        } else coroutineScope.launch { changeToNextStep(true) }
    }
    val focusRequester = remember { FocusRequester() }
    val animatedBackgroundRadius by animateFloatAsState(
        targetValue = if (isDone) 200f else 1f,
        label = "background animation",
        animationSpec = tween(500, easing = FastOutSlowInEasing),
    )

    LaunchedEffect(showDescriptionDialog) {
        if (showDescriptionDialog) {
            focusRequester.requestFocus()
        }
    }
    ListenKeyEvents { keyCode, event ->
        if (event.repeatCount == 0) {
            when (keyCode) {
                KeyEvent.KEYCODE_STEM_1,
                KeyEvent.KEYCODE_STEM_2,
                KeyEvent.KEYCODE_STEM_3,
                    -> {
                    startButtonOnClick()
                    true
                }

                else -> false
            }
        } else {
            false
        }
    }
    Dialog(
        visible = showDescriptionDialog,
        onDismissRequest = { showDescriptionDialog = false },
    ) {
        AlertDialogContent(
            contentPadding = PaddingValues(18.dp),
            icon = {
                Icon(
                    painter = painterResource(id = recipe.recipeIcon.icon),
                    contentDescription = "",
                )
            },
            title = { Text(text = recipe.name) },
        ) {
            item {
                Text(text = recipe.description)
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
    AmbientAware { ambientStateUpdate ->
        val isAmbient = ambientStateUpdate.isAmbient

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(CircularProgressIndicatorDefaults.FullScreenPadding),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier,
                onDraw = {
                    val radialGradient = Brush.radialGradient(
                        center = Offset(0f, 0f),
                        radius = animatedBackgroundRadius,
                        colors = listOf(Color.DarkGray, Color.Transparent),
                        tileMode = TileMode.Clamp,
                    )
                    drawCircle(
                        center = Offset(0f, 0f),
                        radius = animatedBackgroundRadius,
                        brush = radialGradient,
                    )
                },
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize(),
                progress = { animatedProgressValue.value },
                allowProgressOverflow = true,
                colors = ProgressIndicatorDefaults.colors(
                    indicatorColor = if (isAmbient) {
                        Color.White
                    } else {
                        animatedProgressColor.value
                    },
                    trackColor = if (isAmbient) {
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                    } else {
                        animatedProgressColor.value.copy(alpha = 0.2f)
                    },
                ),
                startAngle = 300f,
                endAngle = 240f,
            )
            Column(
                modifier = Modifier.fillMaxRectangle(),
                Arrangement.SpaceBetween,
                Alignment.CenterHorizontally,
            ) {
                AnimatedContent(
                    targetState = Pair(currentStep, isDone), label = "Timer Content",
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    modifier = Modifier.animateContentSize(),
                ) { (currentStep, isDone) ->
                    when {
                        isDone -> {
                            Column(
                                modifier = Modifier
                                    .animateContentSize()
                                    .padding(top = CircularProgressIndicatorDefaults.largeStrokeWidth),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = stringResource(id = R.string.timer_enjoy),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_coffee),
                                    contentDescription = "",
                                )
                            }
                        }

                        currentStep != null -> {
                            Column {
                                TimeText(
                                    currentStep = currentStep,
                                    animatedProgressValue = animatedProgressValue.value * timeMultiplier,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    style = MaterialTheme.typography.titleMedium,
                                    paddingHorizontal = 2.dp,
                                    showMillis = false,
                                )
                                AnimatedContent(currentStep, label = "Step label") {
                                    val style = MaterialTheme.typography.titleSmall.copy(
                                        textMotion = TextMotion.Animated,
                                    )
                                    var textStyle by remember { mutableStateOf(style) }
                                    var readyToDraw by remember { mutableStateOf(false) }
                                    val maxLines = if (it.time != null && it.value != null) {
                                        1
                                    } else {
                                        2
                                    }
                                    Text(
                                        text = if (it.time != null) {
                                            stringResource(
                                                id = R.string.timer_step_name_time,
                                                it.name,
                                                ((it.time!! * timeMultiplier) / 1000).toStringShort(),
                                            )
                                        } else {
                                            it.name
                                        },
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = textStyle,
                                        onTextLayout = { textLayoutResult: TextLayoutResult ->
                                            if (textLayoutResult.hasVisualOverflow ||
                                                textLayoutResult.lineCount > maxLines
                                            ) {
                                                textStyle = textStyle.copy(
                                                    fontSize = textStyle.fontSize * 0.9,
                                                )
                                            } else {
                                                readyToDraw = true
                                            }
                                        },
                                        textAlign = TextAlign.Center,
                                        overflow = TextOverflow.Visible,
                                        modifier = Modifier
                                            .testTag("timer_name")
                                            .fillMaxWidth()
                                            .drawWithContent {
                                                if (readyToDraw) drawContent()
                                            },
                                    )
                                }
                                TimerValue(
                                    currentStep = currentStep,
                                    animatedProgressValue = animatedProgressValue.value,
                                    weightMultiplier = weightMultiplier,
                                    alreadyDoneWeight = alreadyDoneWeight,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        }

                        else -> {
                            Box(contentAlignment = Alignment.Center) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        text = recipe.name,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = if (recipe.description.isNotBlank()) 1 else 2,
                                        textAlign = TextAlign.Center,
                                        style = if (recipe.description.isNotBlank())
                                            MaterialTheme.typography.titleMedium else
                                            MaterialTheme.typography.titleLarge,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                    if (recipe.description.isNotBlank()) {
                                        val sizeOfText = with(LocalDensity.current) {
                                            val fontScale = this.fontScale
                                            val textSize = 14 / fontScale
                                            textSize.sp
                                        }
                                        OutlinedButton(
                                            onClick = { showDescriptionDialog = true },
                                            modifier = Modifier
                                                .padding(top = 8.dp)
                                                .height(ButtonDefaults.CompactButtonHeight)
                                                .fillMaxWidth(),
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.recipe_details_read_description),
                                                modifier = Modifier.padding(horizontal = 8.dp),
                                                fontSize = sizeOfText,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    modifier = Modifier.animateContentSize(),
                    visible = !isAmbient, enter = fadeIn(), exit = fadeOut(),
                ) {
                    Spacer(Modifier.height(12.dp))
                    StartFAB(isTimerRunning, startButtonOnClick)
                }
            }
        }
    }
}
