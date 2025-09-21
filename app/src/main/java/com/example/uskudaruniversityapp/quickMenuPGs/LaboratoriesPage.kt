package com.example.uskudaruniversityapp.quickMenuPGs


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class Laboratory(
    val id: String,
    val name: String,
    val infoUrl: String,
)

val allLaboratories = listOf(
    Laboratory(
        "L001",
        "Podology Laboratory",
        "https://uskudar.edu.tr/tr/icerik/1206/podoloji-laboratuvari"
    ),
    Laboratory(
        "L002",
        "Hair Care and Beauty Services Laboratory",
        "https://uskudar.edu.tr/tr/icerik/1205/sac-bakimi-ve-guzellik-hizmetleri-laboratuvari"
    ),
    Laboratory(
        "L003",
        "Orthodontics Laboratory",
        "https://uskudar.edu.tr/tr/icerik/1204/ortodonti-laboratuvari"
    ),
    Laboratory(
        "L004",
        "Clinical Exercise Center Laboratory",
        "https://uskudar.edu.tr/tr/icerik/1203/klinik-egzersiz-merkezi-laboratuvari"
    ),
    Laboratory(
        "L005",
        "VSHS Research Laboratory",
        "https://uskudar.edu.tr/tr/icerik/1202/vshs-arastirma-laboratuvari"
    ),
    Laboratory("L006", "Advanced Neuroscience Laboratory", "https://example.com/neuroscience"),
    Laboratory("L007", "Biotechnology Research Lab", "https://example.com/biotech"),
    Laboratory("L008", "Robotics and AI Laboratory", "https://example.com/robotics"),
    Laboratory("L009", "Environmental Sciences Lab", "https://example.com/environmental"),
    Laboratory("L010", "Forensic Science Lab", "https://example.com/forensic")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaboratoriesPage(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredLabs = remember(searchQuery) {
        if (searchQuery.isBlank()) allLaboratories
        else allLaboratories.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search Laboratory") },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredLabs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No labs found.",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredLabs) { lab ->
                        LaboratoryListItem(laboratory = lab)
                    }
                }
            }
        }
    }
}

@Composable
fun LaboratoryListItem(laboratory: Laboratory) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = { launchBrowser(context, laboratory.infoUrl) }),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Science,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = laboratory.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { launchBrowser(context, laboratory.infoUrl) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("More info", fontWeight = FontWeight.Medium)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}

private fun launchBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {

    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewLaboratoriesPage() {
    MaterialTheme {
        LaboratoriesPage(rememberNavController())
    }
}