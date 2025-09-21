package com.example.uskudaruniversityapp.quickMenuPGs


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

data class Book(val id: String, val title: String, val author: String, val year: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBooksPage(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = remember { mutableStateListOf<Book>() }

    val performSearch: (String) -> Unit = { query ->
        searchResults.clear()
        if (query.isNotBlank()) {
            val dummyBooks = listOf(
                Book("B001", "The Great Gatsby", "F. Scott Fitzgerald", "1925"),
                Book("B002", "1984", "George Orwell", "1949"),
                Book("B003", "To Kill a Mockingbird", "Harper Lee", "1960"),
                Book("B004", "The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "1979"),
                Book("B005", "Pride and Prejudice", "Jane Austen", "1813"),
                Book("B006", "The Lord of the Rings", "J.R.R. Tolkien", "1954"),
                Book("B007", "Dune", "Frank Herbert", "1965"),
                Book("B008", "Crime and Punishment", "Fyodor Dostoevsky", "1866"),
                Book("B009", "The Odyssey", "Homer", "-800 BC"),
                Book("B010", "The Hobbit", "J.R.R. Tolkien", "1937")
            )
            searchResults.addAll(dummyBooks.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.author.contains(query, ignoreCase = true)
            })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Books") },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by title or author") },
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = { performSearch(searchQuery) },
                modifier = Modifier.fillMaxWidth(),
                enabled = searchQuery.isNotBlank()
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Search")
            }

            Spacer(modifier = Modifier.height(16.dp))


            if (searchResults.isEmpty() && searchQuery.isNotBlank()) {
                Text(
                    text = "No results found for \"$searchQuery\".",
                    modifier = Modifier.fillMaxWidth(),

                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            } else if (searchResults.isEmpty()) {
                Text(
                    text = "Start typing to search for books.",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { book ->
                        BookResultCard(book = book) { clickedBook ->

                            println("Book clicked: ${clickedBook.title}")

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookResultCard(book: Book, onClick: (Book) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(book) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "by ${book.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Text(
                    text = "Published: ${book.year}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            IconButton(onClick = { /* Handle add to list logic here */ }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add to list",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun PreviewSearchBooksPage() {
    MaterialTheme {
//        SearchBooksPage(navController)
    }
}