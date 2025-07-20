@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.omelan.cofi.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.omelan.cofi.ui.*

enum class ItemShape(val shape: RoundedCornerShape) {
    First(firstItem),
    Middle(middleItem),
    Last(lastItem),
    Only(aloneItem), // Used when there is only one item in the list
}

@Composable
fun ListItemBackground(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: () -> Unit,
    shape: ItemShape,
    content: @Composable ColumnScope.() -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
        shape = shape.shape,
    ) {
        Column(
            modifier
                .clickable(
                    onClick = onClick,
                    role = Role.Button,
                )
                .padding(contentPadding),
            content = content,
        )
    }
}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String? = null,
    shape: ItemShape = ItemShape.Middle,
    @DrawableRes iconResource: Int? = null,
    iconVector: ImageVector? = null,
    onPress: () -> Unit,
    extraContent: (@Composable ColumnScope.() -> Unit)? = null,
) {
    ListItemBackground(
        modifier = modifier,
        shape = shape,
        onClick = { onPress() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val modifier = Modifier
                .clip(RoundedCornerShape(100))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(Spacing.normal)
                .size(28.dp)
            if (iconVector != null) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier,
                )
            } else if (iconResource != null) {
                Icon(
                    painter = painterResource(id = iconResource),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier,
                )
            }
            Column(
                modifier = Modifier.padding(
                    vertical = Spacing.big,
                    horizontal = Spacing.medium,
                ),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMediumEmphasized,
                )
                if (subTitle?.isNotBlank() == true) {
                    Text(
                        text = subTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        extraContent?.let { extraContent ->
            HorizontalDivider(Modifier.padding(horizontal = Spacing.big))
            extraContent()
        }
    }
}
