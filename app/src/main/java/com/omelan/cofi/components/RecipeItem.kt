@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.omelan.cofi.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.omelan.cofi.R
import com.omelan.cofi.share.model.Recipe
import com.omelan.cofi.share.model.Step
import com.omelan.cofi.share.model.StepType
import com.omelan.cofi.share.utils.toMillis

@Composable
fun LazyGridItemScope.RecipeItem(
    recipe: Recipe,
    shape: ItemShape = ItemShape.Middle,
    allSteps: List<Step> = emptyList(),
    onPress: (recipeId: Int) -> Unit,
) {
    ListItem(
        modifier = Modifier.animateItem(),
        title = recipe.name,
        subTitle = recipe.description,
        shape = shape,
        iconResource = recipe.recipeIcon.icon,
        onPress = { onPress(recipe.id) },
    )  {
        RecipeInfo(compactStyle = true, steps = allSteps)
    }
}

@Preview
@Composable
fun PreviewRecipeItem() {
    LazyVerticalGrid(columns = GridCells.Fixed(1)) {
        item {
            RecipeItem(
                shape = ItemShape.Middle,
                recipe = Recipe(
                    id = 0,
                    name = "Ultimate V60",
                    description = "Recipe by Hoffman",
                ),
                allSteps = listOf(
                    Step(
                        name = stringResource(R.string.prepopulate_step_coffee),
                        value = 30f,
                        time = 5.toMillis(),
                        type = StepType.ADD_COFFEE,
                    ),
                    Step(
                        name = stringResource(R.string.prepopulate_step_water),
                        value = 60f,
                        time = 5.toMillis(),
                        type = StepType.WATER,
                    ),
                    Step(
                        name = stringResource(R.string.prepopulate_step_swirl),
                        time = 5.toMillis(),
                        type = StepType.OTHER,
                    ),
                    Step(
                        name = stringResource(R.string.prepopulate_step_wait),
                        time = 35.toMillis(),
                        type = StepType.WAIT,
                    ),
                    Step(
                        name = stringResource(R.string.prepopulate_step_water),
                        time = 30.toMillis(),
                        type = StepType.WATER,
                        value = 240f,
                    ),
                ),
                onPress = {},
            )
        }
    }
}
