
package com.omelan.cofi.wearos.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.*
import androidx.wear.tooling.preview.devices.WearDevices
import com.omelan.cofi.share.model.Recipe

@Composable
private fun RecipeListItemRaw(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int? = null,
    text: String = "",
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.height(ButtonDefaults.Height),
        onClick = onClick,
    ) {
        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically) {
            Box {
                if (iconRes != null) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = text,
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun RecipeListItem(modifier: Modifier = Modifier, recipe: Recipe?, onClick: () -> Unit = {}) {
    val placeholderState = rememberPlaceholderState(
        recipe?.name != null,
    )
    AnimatedContent(
        targetState = recipe,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "RecipeListItemAnimation",
    ) {
        if (it != null) {
            RecipeListItemRaw(
                modifier.placeholderShimmer(placeholderState),
                onClick = onClick,
                text = it.name,
                iconRes = it.recipeIcon.icon,
            )
        } else {
            RecipeListItemRaw(
                modifier.placeholderShimmer(placeholderState),
                onClick = onClick,
                text = "",
            )
        }
    }


}

@Preview(device = WearDevices.SMALL_ROUND)
@Composable
fun RecipeListItemPreview() {
    Column {
        RecipeListItem(recipe = Recipe(id = 0, name = "test"))
        RecipeListItem(recipe = null)
    }
}
