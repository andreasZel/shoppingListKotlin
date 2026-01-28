package eu.tutorial.shoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int
);

data class EditHandler(
    var isOpen: Boolean = false,
    var state: String = "create",
    var selectedItemId: Int? = null
);

@Composable
fun ListItem(item: ShoppingItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xff64b5f6)),
                shape = RoundedCornerShape(20)
            )
    ) {
        Row {
            Text("Name: ${item.name}")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Quantity: ${item.quantity.toString()}")
        }
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Icon")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Delete Icon")
            }
        }
    }
}

@Composable
fun ShoppingListApp() {
    var shopping_items by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var dialogState by remember { mutableStateOf("create") }
    var selectedItemId: Int? by remember { mutableStateOf(null) }
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
            isDialogOpen = false
            inputValueName = ""
            inputValueQuantity = ""
        }
    }

    fun saveChanges() {
        if (inputValueName != "" && inputValueQuantity != "" && selectedItemId != null) {

            shopping_items = shopping_items.map { item ->
                if (item.id == selectedItemId) {
                    item.copy(
                        name = inputValueName,
                        quantity = inputValueQuantity.toIntOrNull() ?: 0
                    )
                } else item
            }
            isDialogOpen = false
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
            onClick = {
                dialogState = "create"
                isDialogOpen = true
            },
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
                ListItem(
                    it,
                    {
                        inputValueName = it.name
                        inputValueQuantity = it.quantity.toString()
                        dialogState = "edit"
                        isDialogOpen = true
                        selectedItemId = it.id
                    }, {
                        shopping_items = shopping_items.minusElement(it)
                    })
            }
        }
    }

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(onClick = {
                        if (dialogState == "create") {
                            addItem()
                        } else {
                            saveChanges()
                        }
                    }, modifier = Modifier.padding(4.dp)) {
                        Text(if (dialogState == "create") "Add Item" else "Save")
                    }
                    Button(
                        onClick = { isDialogOpen = false; dialogState = "create" },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("Cancel")
                    }
                }
            },
            title = { Text(if (dialogState == "create") "Add Shopping Item" else "Edit Shopping Item") },
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