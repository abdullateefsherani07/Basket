package com.android.bucket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.bucket.ui.theme.BucketTheme

data class ShoppingListItem(
    var id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BucketApp(){
    var sItems by remember{ mutableStateOf(listOf<ShoppingListItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text("Bucket")
                }
            )
        },
        bottomBar = {
            var selectedItem by remember { mutableStateOf(0) }
            val item = listOf(
                BottomNavigationItem(
                    "Home",
                    Icons.Filled.Home,
                    Icons.Outlined.Home
                ),
                BottomNavigationItem(
                    "Search",
                    Icons.Filled.Search,
                    Icons.Outlined.Search
                ),
                BottomNavigationItem(
                    "Updates",
                    Icons.Filled.Notifications,
                    Icons.Outlined.Notifications
                ),
                BottomNavigationItem(
                    "More",
                    Icons.Filled.MoreVert,
                    Icons.Outlined.MoreVert
                )
            )
            NavigationBar {
                item.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector =
                                if (index == selectedItem){
                                    item.selectedIcon
                                }else{
                                    item.unSelectedIcon
                                }, "")
                               },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        label = { Text(item.title)}
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add New Item")
            }
        },
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center
        ){
            if (sItems.isNotEmpty()){
                Row(
                    modifier = Modifier
                        .padding(16.dp, 16.dp, 16.dp, 0.dp)
                        .fillMaxWidth()
                        .border(
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(20)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(20)
                        )
                ){
                    Text(
                        text = "S",
                        modifier = Modifier
                            .padding(15.dp, 8.dp, 8.dp, 8.dp)
                            .width(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Name",
                        modifier = Modifier
                            .padding(8.dp)
                            .width(110.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Q",
                        modifier = Modifier
                            .padding(15.dp, 8.dp, 8.dp, 8.dp)
                            .width(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                items(sItems){
                    item ->
                    if (item.isEditing){
                        ShoppingItemEditor(item = item, onEditComplete = { editedName, editedQuantity ->
                            sItems = sItems.map{it.copy(isEditing = false)}
                            val editedItem = sItems.find{ it.id == item.id}
                            editedItem?.let{
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        })
                    } else{
                        ShoppingItem(
                            item = item,
                            onEditClick = {
                                sItems = sItems.map{it.copy(isEditing = it.id == item.id)}
                            },
                            onDeleteClick = {
                                sItems = sItems - item
                            }
                        )
                    }
                }
            }
        }

    }

    if (showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    if (itemName.isNotBlank() && itemQuantity.isNotBlank()){
                        val newItem = ShoppingListItem(
                            id =sItems.size + 1,
                            name = itemName,
                            quantity = itemQuantity.toInt()
                        )
                        sItems += newItem
                        showDialog = false
                        itemName = ""
                        itemQuantity = ""
                    }
                }) {
                    Text("Add Item")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text(text = "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        label = { Text("Name") },
                        value = itemName,
                        onValueChange = { itemName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        label = { Text("Quantity")},
                        value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingListItem, onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(15)
            ),
    ) {
        Row {
            OutlinedTextField(
                value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                shape = RoundedCornerShape(20),
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f)
            )
            OutlinedTextField(
                value = editedQuantity,
                onValueChange = {editedQuantity = it},
                singleLine = true,
                shape = RoundedCornerShape(20),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(8.dp, 4.dp)
        ) {
            Button(
                onClick = {
                    isEditing = false
                    onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20)
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun ShoppingItem(
    item: ShoppingListItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .padding(0.dp, 10.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(20)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = item.id.toString(),
            modifier = Modifier
                .padding(15.dp, 8.dp, 8.dp, 8.dp)
                .width(16.dp)
        )
        Text(
            text = item.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(8.dp)
                .width(110.dp)
        )
        Text(
            text = item.quantity.toString(),
            modifier = Modifier
                .padding(15.dp, 8.dp, 8.dp, 8.dp)
        )
        Row (
            modifier = Modifier.width(140.dp),
            horizontalArrangement = Arrangement.End
        ){
            IconButton(
                onClick = onEditClick,
                modifier = Modifier.width(50.dp),
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Filled.Edit, contentDescription = "")
            }
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.width(50.dp),
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BucketAppPreview() {
    BucketTheme {
        BucketApp()
    }
}
