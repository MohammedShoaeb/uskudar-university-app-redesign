package com.example.uskudaruniversityapp.quickMenuPGs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlin.math.roundToInt
data class Program(val name: String, val baseFee: Double)
data class Scholarship(val name: String, val discountPercentage: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WageCalculationPage(navController: NavController) {
    val programs = remember {
        listOf(
            Program("Please Select", 0.0),
            Program("Computer Engineering", 100000.0),
            Program("Psychology", 90000.0),
            Program("Architecture", 110000.0),
            Program("Law", 120000.0),
            Program("Medicine", 150000.0)
        )
    }

    val scholarships = remember {
        listOf(
            Scholarship("No Discount", 0),
            Scholarship("Merit Scholarship", 25),
            Scholarship("Sibling Discount", 10),
            Scholarship("Early Bird Discount", 5),
            Scholarship("Sport Scholarship", 15),
            Scholarship("Exam Success (OSYS) 50%", 50),
            Scholarship("Exam Success (OSYS) 75%", 75),
            Scholarship("Exam Success (OSYS) 100%", 100)
        )
    }

    var selectedProgram by remember { mutableStateOf(programs[0]) }
    var selectedDiscount1 by remember { mutableStateOf(scholarships[0]) }
    var selectedDiscount2 by remember { mutableStateOf(scholarships[0]) }
    var selectedDiscount3 by remember { mutableStateOf(scholarships[0]) }

    var programExpanded by remember { mutableStateOf(false) }
    var discount1Expanded by remember { mutableStateOf(false) }
    var discount2Expanded by remember { mutableStateOf(false) }
    var discount3Expanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val vatRate = 0.10
    val calculatedTotal = remember(selectedProgram, selectedDiscount1, selectedDiscount2, selectedDiscount3) {
        if (selectedProgram.name == "Please Select") {
            0.0
        } else {
            var total = selectedProgram.baseFee
            total *= (1 - selectedDiscount1.discountPercentage / 100.0)
            total *= (1 - selectedDiscount2.discountPercentage / 100.0)
            total *= (1 - selectedDiscount3.discountPercentage / 100.0)
            total * (1 + vatRate)
        }
    }

    Scaffold {
            paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Program/Bolum",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            SelectionRow(
                label = "Please Select",
                value = selectedProgram.name,
                percentage = if (selectedProgram.name != "Please Select") "--%" else "--%",
                expanded = programExpanded,
                onExpandedChange = { programExpanded = it },
                options = programs.map { it.name },
                onOptionSelected = { name ->
                    selectedProgram = programs.find { it.name == name } ?: programs[0]
                }
            )


            Text(
                text = "Scholarship/ discount 1",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            SelectionRow(
                label = "Please Select",
                value = selectedDiscount1.name,
                percentage = "${selectedDiscount1.discountPercentage}%",
                expanded = discount1Expanded,
                onExpandedChange = { discount1Expanded = it },
                options = scholarships.map { it.name },
                onOptionSelected = { name ->
                    selectedDiscount1 = scholarships.find { it.name == name } ?: scholarships[0]
                }
            )

            Text(
                text = "Scholarship/ discount 2",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            SelectionRow(
                label = "Please Select",
                value = selectedDiscount2.name,
                percentage = "${selectedDiscount2.discountPercentage}%",
                expanded = discount2Expanded,
                onExpandedChange = { discount2Expanded = it },
                options = scholarships.map { it.name },
                onOptionSelected = { name ->
                    selectedDiscount2 = scholarships.find { it.name == name } ?: scholarships[0]
                }
            )

            Text(
                text = "Scholarship/ discount 3",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            SelectionRow(
                label = "Please Select",
                value = selectedDiscount3.name,
                percentage = "${selectedDiscount3.discountPercentage}%",
                expanded = discount3Expanded,
                onExpandedChange = { discount3Expanded = it },
                options = scholarships.map { it.name },
                onOptionSelected = { name ->
                    selectedDiscount3 = scholarships.find { it.name == name } ?: scholarships[0]
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Total:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth() ,

                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${calculatedTotal.roundToInt()} TL",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }


            Spacer(modifier = Modifier.height(4.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                text = "* 10% VAT is included in the calculations.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.fillMaxWidth(),

                textAlign = TextAlign.Start,
                text = "* The calculated amounts are for informational purposes only. They do not constitute the exact registration fee.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                modifier = Modifier.fillMaxWidth(),

                textAlign = TextAlign.Start,

                text = "* The calculations are based on the program fees for departments where the language of instruction is English.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )


            val scholarshipAnnotatedString = buildAnnotatedString {
                append("* For detailed information about Scholarship Opportunities ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Click Here.")
                }
            }
            ClickableText(
                text = scholarshipAnnotatedString,
                onClick = { /* no-op */ },
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )

            val feesAnnotatedString = buildAnnotatedString {
                append("* For Fees and Payment Conditions ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Click Here.")
                }
            }
            ClickableText(
                text = feesAnnotatedString,
                onClick = { /* no-op */ },
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionRow(
    label: String,
    value: String,
    percentage: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier.weight(1f)
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .width(60.dp)
                .height(56.dp),
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = percentage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewWageCalculationPage() {
    MaterialTheme {
        WageCalculationPage(rememberNavController())
    }
}