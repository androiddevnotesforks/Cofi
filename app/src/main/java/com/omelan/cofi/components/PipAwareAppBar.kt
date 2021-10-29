package com.omelan.cofi.components

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.omelan.cofi.LocalPiPState
import com.omelan.cofi.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun createAppBarBehavior(): TopAppBarScrollBehavior {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    return scrollBehavior
}

@Composable
fun PiPAwareAppBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {
        Text(
            text = stringResource(id = R.string.app_name),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    },
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.background,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    elevation: Dp = 0.dp
) {
    if (!LocalPiPState.current) {
        InsetAwareTopAppBar(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            backgroundColor = backgroundColor,
            elevation = elevation,
            scrollBehavior = scrollBehavior,
        )
    }
}

@Composable
fun InsetAwareTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit) = {},
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.surface,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    elevation: Dp = 4.dp
) {

//    Surface(
//        color = backgroundColor,
//        elevation = elevation,
//        modifier = modifier
//    ) {
        LargeTopAppBar(
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            modifier = Modifier.statusBarsPadding(),
            scrollBehavior = scrollBehavior,
        )
//    }
}

@Composable
@Preview
fun PiPAwareAppBarPreview() {
    InsetAwareTopAppBar(title = { Text(text = "test")})
}