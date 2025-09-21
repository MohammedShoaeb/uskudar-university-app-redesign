package com.example.uskudaruniversityapp.quickMenuPGs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.uskudaruniversityapp.R

// Data class to hold campus information
data class CampusInfo(
    val name: String,
    val locationDescription: String,
    val phoneNumber: String,
    val contactEmail: String,
    val mapUri: String,
    val imageUrl: Int? = null,
)

val campusList = listOf(
    CampusInfo(
        name = "Altunizade Merkez Yerleske",
        locationDescription = "Altunizade Campus",
        phoneNumber = "+905259029372",
        contactEmail = "info@uskudar.edu.tr",
        mapUri = "geo:41.026723,29.020580?q=Üsküdar Üniversitesi Altunizade Merkez Kampüsü",
        imageUrl = R.drawable.altunizade
    ),
    CampusInfo(
        name = "Uskudar Carsi Yerleske",
        locationDescription = "Üsküdar Çarşı Campus",
        phoneNumber = "+905259029372",
        contactEmail = "info@uskudar.edu.tr",
        mapUri = "geo:41.025211,29.015250?q=Üsküdar Üniversitesi Çarşı Yerleşkesi",
        imageUrl = R.drawable.carsi
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusesPage(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(campusList) { campus ->
            CampusInfoCard(campus = campus)
        }
    }
}

@Composable
fun CampusInfoCard(campus: CampusInfo) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = campus.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (campus.imageUrl != null) {

                    Image(
                        painter = painterResource(id = campus.imageUrl),
                        contentDescription = "Map or Photo of ${campus.name}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = "Image Placeholder",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Location icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = campus.locationDescription,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    val gmmIntentUri = Uri.parse(campus.mapUri)
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    if (mapIntent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(mapIntent)
                    } else {
                        val webMapIntent = Intent(
                            Intent.ACTION_VIEW,
                            "https://maps.google.com/?q=${Uri.encode(campus.locationDescription)}".toUri()
                        )
                        context.startActivity(webMapIntent)
                    }
                }) {
                    Text("View Map", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Call,
                    contentDescription = "Call icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = campus.phoneNumber,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = "tel:${campus.phoneNumber}".toUri()
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        println("No app found to handle dialing.") // For debugging
                    }
                }) {
                    Text("Call Now", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Contact Us",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data =
                            "mailto:".toUri()
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(campus.contactEmail))
                        putExtra(Intent.EXTRA_SUBJECT, "Inquiry about ${campus.name}")
                    }
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {

                        println("No email app found to handle the intent.")
                    }
                }) {
                    Text("Contact us", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewCampusesPage() {
    MaterialTheme {
        CampusesPage(rememberNavController())
    }
}