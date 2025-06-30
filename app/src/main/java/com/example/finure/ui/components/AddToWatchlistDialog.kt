package com.finure.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

/**
 * AlertDialog used for adding a stock to a watchlist.
 * Supports selecting an existing watchlist or creating a new one.
 *
 * @param existingWatchlists List of user’s existing watchlists.
 * @param onAdd Callback when user confirms addition with selected or new name.
 * @param onDismiss Callback when dialog is dismissed without action.
 */
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
                // Dropdown list for selecting existing watchlists
                if (existingWatchlists.isNotEmpty()) {
                    Text("Select from existing")
                    DropdownMenu(
                        expanded = true, // ⚠️ You might want to control this with a state
                        onDismissRequest = {},
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        existingWatchlists.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = { selectedWatchlist = it }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input field to create a new watchlist
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
