package com.example.uskudaruniversityapp.quickMenuPGs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.core.net.toUri
import androidx.navigation.NavController

private fun launchBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

private fun launchDialer(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
    }
}

private fun launchWhatsApp(context: Context, phoneNumber: String, message: String = "") {
    try {
        val uri = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.whatsapp")
        context.startActivity(intent)
    } catch (e: Exception) {
        val webUri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}")
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        if (webIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(webIntent)
        } else {
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactCenterPage(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ContactCard(
                    icon = Icons.Default.Call,
                    iconTint = MaterialTheme.colorScheme.tertiary,
                    title = "Call Center",
                    description = "It is also possible for you to find out what you are curious about our university by phone. The representatives of our Call Center number 0216 400 22 22 are waiting for you to answer all the questions you have in mind."
                ) {
                    ContactButton(
                        icon = Icons.Default.Call,
                        text = "Call Now",
                        onClick = { launchDialer(context, "02164002222") }
                    )
                }
            }

            item {
                ContactCard(
                    icon = Icons.Default.Whatsapp,
                    iconTint = Color(0xFF25D366),
                    title = "WhatsApp Support",
                    description = "You can also contact us via WhatsApp application to ask if you are curious about our university. It is also possible to access our WhatsApp line from 0216 400 22 22."
                ) {
                    ContactButton(
                        icon = Icons.Default.Whatsapp,
                        text = "Send Message",
                        onClick = { launchWhatsApp(context, "902164002222", "Hello, I have a question about...") }
                    )
                }
            }

            item {
                ContactCard(
                    icon = Icons.AutoMirrored.Filled.Chat,
                    iconTint = MaterialTheme.colorScheme.secondary,
                    title = "Live Support",
                    description = "We answer your questions instantly with our online live support system. You can contact our preference consultants and students by entering their information in the box in the lower right corner of our website."
                ) {
                    ContactButton(
                        icon = Icons.AutoMirrored.Filled.Chat,
                        text = "Start Live Chat",
                        onClick = { launchBrowser(context, "https://example.com/live-chat") }
                    )
                }
            }

            item {
                ContactCard(
                    icon = Icons.Default.ListAlt,
                    iconTint = MaterialTheme.colorScheme.primary,
                    title = "Information Form",
                    description = "If you fill out our information form to contact us, we will reach you immediately."
                ) {
                    ContactButton(
                        icon = Icons.Default.ListAlt,
                        text = "Start Your Request",
                        onClick = { launchBrowser(context, "https://example.com/information-form") }
                    )
                }
            }
        }
    }
}

@Composable
fun ContactCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    description: String,
    buttonContent: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth() .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = RoundedCornerShape(12.dp)
        ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = iconTint
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            buttonContent()
        }
    }
}

@Composable
fun ContactButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewContactCenterPage() {
    MaterialTheme {
        ContactCenterPage(rememberNavController())
    }
}