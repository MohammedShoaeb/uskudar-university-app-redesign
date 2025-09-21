package com.example.uskudaruniversityapp.quickMenuPGs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

enum class FoodType {
    MAIN_COURSE, SOUP, DESSERT, SIDE, SALAD, DRINK, OTHER
}

data class FoodItem(
    val nameTurkish: String,
    val nameEnglish: String,
    val calories: Int,
    val icon: ImageVector,
    val type: FoodType
)

data class MealPeriod(
    val name: String,
    val items: List<FoodItem>
)

data class DailyMenu(
    val date: LocalDate,
    val mealPeriods: List<MealPeriod>
)

@RequiresApi(Build.VERSION_CODES.O)
val today = LocalDate.of(2025, 6, 15)

@RequiresApi(Build.VERSION_CODES.O)
val dummyDailyMenus = listOf(
    DailyMenu(
        date = today,
        mealPeriods = listOf(
            MealPeriod("Lunch", listOf(
                FoodItem("Mercimek Çorbası", "Lentil Soup", 150, Icons.Default.Restaurant, FoodType.SOUP)
            )),
            MealPeriod("Dinner", listOf(
                FoodItem("Tavuk Sinitzel", "Chicken Schnitzel", 450, Icons.Default.DinnerDining, FoodType.MAIN_COURSE),
                FoodItem("Pirinç Pilavı", "Rice Pilaf", 200, Icons.Default.Egg, FoodType.SIDE),
                FoodItem("Yoğurtlu Havuç Salatası", "Carrot Salad with Yogurt", 120, Icons.Default.Restaurant, FoodType.SALAD)
            ))
        )
    ),
    DailyMenu(
        date = today.plusDays(1),
        mealPeriods = listOf(
            MealPeriod("Lunch", listOf(
                FoodItem("Ezogelin Çorbası", "Ezogelin Soup", 160, Icons.Default.Restaurant, FoodType.SOUP)
            )),
            MealPeriod("Dinner", listOf(
                FoodItem("Kuru Fasulye", "Dried Beans", 400, Icons.Default.Fastfood, FoodType.MAIN_COURSE),
                FoodItem("Bulgur Pilavı", "Bulgur Pilaf", 220, Icons.Default.Egg, FoodType.SIDE),
                FoodItem("Sütlaç", "Rice Pudding", 180, Icons.Default.Cake, FoodType.DESSERT)
            ))
        )
    ),
    DailyMenu(
        date = today.plusDays(2),
        mealPeriods = listOf(
            MealPeriod("Dinner", listOf(
                FoodItem("İzmir Köfte", "Izmir Meatballs", 480, Icons.Default.DinnerDining, FoodType.MAIN_COURSE),
                FoodItem("Makarna", "Pasta", 250, Icons.Default.Fastfood, FoodType.SIDE)
            ))
        )
    ),
    DailyMenu(
        date = today.plusDays(3),
        mealPeriods = listOf(
            MealPeriod("Lunch", listOf(
                FoodItem("Tavuk Sote", "Chicken Saute", 350, Icons.Default.Fastfood, FoodType.MAIN_COURSE)
            )),
            MealPeriod("Dinner", listOf(
                FoodItem("Balık Tava", "Pan-fried Fish", 390, Icons.Default.DinnerDining, FoodType.MAIN_COURSE),
                FoodItem("Patates Püresi", "Mashed Potatoes", 180, Icons.Default.Egg, FoodType.SIDE),
                FoodItem("Mevsim Salata", "Seasonal Salad", 70, Icons.Default.Restaurant, FoodType.SALAD)
            ))
        )
    ),
    DailyMenu(
        date = today.plusDays(4),
        mealPeriods = listOf(
            MealPeriod("Dinner", listOf(
                FoodItem("Pide Çeşitleri", "Pide Varieties", 550, Icons.Default.Fastfood, FoodType.MAIN_COURSE),
                FoodItem("Ayran", "Yogurt Drink", 80, Icons.Default.Restaurant, FoodType.DRINK)
            ))
        )
    ),
    DailyMenu(
        date = today.plusDays(5),
        mealPeriods = listOf(
            MealPeriod("Lunch", listOf(FoodItem("Kapalı", "Closed (Lunch)", 0, Icons.Default.Error, FoodType.OTHER))),
            MealPeriod("Dinner", emptyList())
        )
    ),
    DailyMenu(
        date = today.plusDays(6),
        mealPeriods = listOf(
            MealPeriod("Dinner", emptyList())
        )
    )
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListPage() {
    val context = LocalContext.current


    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var expanded by remember { mutableStateOf(false) }


    val dateOptions = remember {
        val today = LocalDate.now()
        val options = mutableListOf<LocalDate>()

        for (i in 0 until 30) {
            val date = today.plusDays(i.toLong())
            if (date.dayOfWeek != DayOfWeek.SATURDAY && date.dayOfWeek != DayOfWeek.SUNDAY) {
                options.add(date)
            }
        }
        options
    }

    LaunchedEffect(Unit) {
        if (!dateOptions.contains(selectedDate) && dateOptions.isNotEmpty()) {
            selectedDate = dateOptions.first()
        }
    }


    val currentDailyMenu = remember(selectedDate) {
        dummyDailyMenus.find { it.date == selectedDate }?.let { dailyMenu ->
            dailyMenu.copy(mealPeriods = dailyMenu.mealPeriods.filter { it.name == "Dinner" })
        }
    }

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    OutlinedTextField(
                        value = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy (EEEE)", Locale.getDefault())),
                        onValueChange = {  },
                        readOnly = true,
                        label = { Text("Show Meals for:") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        dateOptions.forEach { date ->
                            DropdownMenuItem(
                                text = {
                                    Text(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy (EEEE)", Locale.getDefault())))
                                },
                                onClick = {
                                    selectedDate = date
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }


                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val mealPeriodsToShow = currentDailyMenu?.mealPeriods ?: emptyList()

                    if (mealPeriodsToShow.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Restaurant,
                                    contentDescription = "No Meals Available",
                                    modifier = Modifier.size(72.dp),
                                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                )
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "No Dinner meals scheduled for ${selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault()))}.",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = if (selectedDate.dayOfWeek == DayOfWeek.SATURDAY || selectedDate.dayOfWeek == DayOfWeek.SUNDAY) {
                                        "Dining hall is closed on weekends."
                                    } else {
                                        "Please check another date or contact the dining hall."
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        items(mealPeriodsToShow) { mealPeriod ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = mealPeriod.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column {
                                        mealPeriod.items.forEachIndexed { index, item ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(if (index % 2 == 0) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface)
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = item.icon,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(Modifier.width(16.dp))
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = item.nameTurkish,
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = item.nameEnglish,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                                Column(horizontalAlignment = Alignment.End) {
                                                    Text(
                                                        text = "${item.calories}",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.secondary
                                                    )
                                                    Text(
                                                        text = "KCAL",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                            if (index < mealPeriod.items.size - 1) {
                                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun PreviewFoodListPage() {
    MaterialTheme {
    }
}