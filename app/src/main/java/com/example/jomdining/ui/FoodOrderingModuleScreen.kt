package com.example.jomdining.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jomdining.R
import com.example.jomdining.data.TempDataSource
import com.example.jomdining.data.TempMenuItems
import com.example.jomdining.data.TempTopic
import com.example.jomdining.databaseentities.Menu

@Composable
fun FoodOrderingModuleScreen(
    // viewModel: JomDiningViewModel,
    modifier: Modifier = Modifier
) {
    // TopicGrid(modifier = modifier)
    MenuItemGrid(modifier = modifier)
}



@Composable
fun MenuItemGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        items(TempMenuItems.menuItems) { menuItem ->
            MenuItemCard(menuItem)
        }
    }
}

@Composable
fun MenuItemCard(
    menuItem: Menu,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    // modify this painter later. it is currently hardcoded.
                    // the Menu table currently does not yet have a column for image name
                    painter = painterResource(id = R.drawable.chickenchop),
                    contentDescription = menuItem.menuItemName,
                    modifier = modifier
                        .size(width = 120.dp, height = 120.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Row {
                Text(
                    text = menuItem.menuItemName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 8.dp
                    )
                )
            }
            Row {
                Text(
                    text = "RM " + String.format("%.2f", menuItem.menuItemPrice),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(74, 22, 136)
                )
            }
        }
    }
}

@Preview
@Composable
fun MenuItemCardPreview() {
    val menuItem =
        Menu(
            menuItemID = 1,
            menuItemName = stringResource(R.string.chicken_chop),
            menuItemPrice = 12.34,
            menuItemType = stringResource(R.string.main_course)
        )
    MenuItemCard(
        menuItem = menuItem,
    )
}

@Preview(
    name = "pixel_tablet_landscape",
    device = "spec: width=2560dp, height=1600dp, orientation=landscape",
    showBackground = true
)
@Composable
fun FoodOrderingModulePreview() {
    val topic = TempTopic(R.string.photography, 1234, R.drawable.photography)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopicCard(topic = topic)
    }
}

/*
    OLD CODE BELOW THIS LINE
*/

@Composable
fun TopicGrid(
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(TempDataSource.topics) { topic ->
            TopicCard(topic)
        }
    }
}

@Composable
fun TopicCard(
    topic: TempTopic,
    modifier: Modifier = Modifier
) {
    Card {
        Row {
            Box {
                Image(
                    painter = painterResource(id = topic.imageRes),
                    contentDescription = stringResource(id = topic.name),
                    modifier = modifier
                        .size(width = 68.dp, height = 68.dp)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = stringResource(id = topic.name),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_grain),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )
                    Text(
                        text = topic.availableCourse.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}