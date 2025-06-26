package com.finure.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun AddToWatchlistDialog(
    existingWatchlists: List<String>,
    onAdd: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedWatchlist by remember { mutableStateOf("") }
    var newWatchlist by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val finalName = if (newWatchlist.isNotBlank()) newWatchlist else selectedWatchlist
                if (finalName.isNotBlank()) onAdd(finalName)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Add to Watchlist") },
        text = {
            Column {
                if (existingWatchlists.isNotEmpty()) {
                    Text("Select from existing")
                    DropdownMenu(
                        expanded = true,
                        onDismissRequest = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        existingWatchlists.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedWatchlist = it
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newWatchlist,
                    onValueChange = { newWatchlist = it },
                    label = { Text("Or create new") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )
            }
        }
    )
}
