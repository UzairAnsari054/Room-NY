package com.example.room_ny

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.room_ny.ui.theme.RoomNYTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userViewModel.refreshUsers()

        enableEdgeToEdge()
        setContent {
            RoomNYTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    UserInputs(
                        onSave = { name, age, surname, createDate, updatedDate ->
                            userViewModel.insertUser(name, age, surname, createDate, updatedDate)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DisplayData(
                        userViewModel,
                        onDelete = {
                            userViewModel.deleteUser(it)
                        },
                        onUpdate = {
                            userViewModel.updateUser(it)
                        },
                        onReplace = { oldUser, newUser ->
                            userViewModel.replaceUser(oldUser, newUser)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                }
            }
        }
    }
}

@Composable
fun UserInputs(
    onSave: (String, String, String, Date, Date) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        var name by remember { mutableStateOf("") }
        var age by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "Enter Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text(text = "Enter Age") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text(text = "Enter Surname") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                onSave(name, age, surname, Date(), Date())
                name = ""
                age = ""

            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save")
        }

    }
}

@Composable
fun DisplayData(
    userViewModel: UserViewModel,
    onDelete: (User) -> Unit,
    onUpdate: (User) -> Unit,
    onReplace: (User, User) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(userViewModel.users) {
            UserItem(
                user = it,
                onDelete = { onDelete(it) },
                onUpdate = { onUpdate(it) },
                onReplace = { oldUser, newUser ->
                    onReplace(oldUser, newUser)
                }
            )
        }
    }
}


@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: User,
    onDelete: (User) -> Unit,
    onUpdate: (User) -> Unit,
    onReplace: (User, User) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = user.userId.toString(), fontSize = 24.sp)
            Text(text = user.firstName, fontSize = 24.sp)
            Text(text = user.age, fontSize = 24.sp)
            IconButton(onClick = { onDelete(user) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
            IconButton(onClick = {
                onUpdate(
                    user.copy(
                        firstName = user.firstName.plus("*"),
                        updatedDate = Date()
                    )
                )
            }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = {
                val newUser =
                    User(user.userId, "SRK", "56", createdDate = Date(), updatedDate = Date())
                onReplace(user, newUser)
            }) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }
        }
        Text(text = formatDate(user.createdDate))
        Text(text = formatDate(user.updatedDate))
    }
}