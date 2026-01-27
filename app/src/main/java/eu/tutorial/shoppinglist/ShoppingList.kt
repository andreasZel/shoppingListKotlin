package eu.tutorial.shoppinglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
);

@Composable
fun ShoppingListApp() {
    var shopping_items by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var inputValueName by remember { mutableStateOf("") }
    var inputValueQuantity by remember { mutableStateOf("") }

    fun addItem() {
        if (inputValueName != "" && inputValueQuantity != "") {
            val newItem = ShoppingItem(
                id = shopping_items.size + 1,
                quantity = inputValueQuantity.toIntOrNull() ?: 0,
                name = inputValueName
            )

            shopping_items = shopping_items + newItem
            showDialog = false
            inputValueName = ""
            inputValueQuantity = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add an Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(shopping_items) {

            }
        }
    }

    if (!showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(onClick = { addItem() }, modifier = Modifier.padding(4.dp)) {
                        Text("Add Item")
                    }
                    Button(onClick = { showDialog = false }, modifier = Modifier.padding(4.dp)) {
                        Text("Cancel")
                    }
                }
            },
            title = { Text("Add Shopping Item") },
            text = {
                Column() {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Name:", modifier = Modifier.width(90.dp))
                            OutlinedTextField(
                                value = inputValueName,
                                onValueChange = { inputValueName = it })
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Quantity:", modifier = Modifier.width(90.dp))
                            OutlinedTextField(
                                value = inputValueQuantity,
                                onValueChange = { inputValueQuantity = it })
                        }
                    }
                }
            }
        )
    }
}