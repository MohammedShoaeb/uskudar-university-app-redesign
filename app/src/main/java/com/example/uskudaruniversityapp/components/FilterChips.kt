package com.example.uskudaruniversityapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uskudaruniversityapp.dataClass.FilterItem



@Composable
fun FilterChip(
    filterItem: FilterItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val backgroundColor = if (isSelected) activeColor else inactiveColor
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else textColor

    Surface(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = "${filterItem.name} (${filterItem.count})",
            color = contentColor,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}




@Composable
fun FilterChipRow(
    filters: List<FilterItem>,
    selectedFilter: FilterItem,
    onFilterSelected: (FilterItem) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()

                }
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(filters) { filterItem ->
            FilterChip(
                filterItem = filterItem,
                isSelected = filterItem == selectedFilter,
                onClick = { onFilterSelected(filterItem) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFilterChipRow() {

    MaterialTheme {
        val allFilters = remember {
            listOf(
                FilterItem("All", 26),
                FilterItem("University Services", 6),
                FilterItem("Academic Information", 7),
                FilterItem("Campus Resources", 5),
                FilterItem("University Media", 2),
                FilterItem("General Information", 2)
            )
        }

        var currentSelectedFilter by remember { mutableStateOf(allFilters.first()) }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Filter Options:",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            FilterChipRow(
                filters = allFilters,
                selectedFilter = currentSelectedFilter,
                onFilterSelected = { selected ->
                    currentSelectedFilter = selected
                    println("Selected filter: ${selected.name}")
                }
            )
        }
    }
}