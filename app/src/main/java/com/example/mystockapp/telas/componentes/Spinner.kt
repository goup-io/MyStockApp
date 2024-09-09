package com.example.mystockapp.telas.componentes

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun <T> Spinner(
    itemList : List<T>,
    selectedItem : T,
    onItemSelected: (selectedItem : T) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    OutlinedButton(onClick = { expanded = true}) {
        Text(
            text = selectedItem.toString(),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f,)
        )
        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }) {
        itemList.forEach {
            DropdownMenuItem(onClick = {
                expanded = false
                onItemSelected(it)
            }) {
                Text(text = it.toString())
            }
        }
    }

}

