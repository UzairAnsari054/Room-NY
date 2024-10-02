package com.example.room_ny

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    var users by mutableStateOf(listOf<User>())
        private set

    init {
        refreshUsers()
    }

    fun insertUser(
        name: String,
        age: String,
        surname: String,
        createdDate: Date,
        updatedAtDate: Date
    ) {
        val newUser = User(
            firstName = name,
            age = age,
            surname = surname,
            createdDate = createdDate,
            updatedDate = updatedAtDate
        )
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertUser(newUser)
            refreshUsers()
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.deleteUser(user)
            refreshUsers()
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.updateUser(user)
            refreshUsers()
        }
    }

    fun replaceUser(oldUser: User, newUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.replaceUser(oldUser, newUser)
            refreshUsers()
        }
    }

    fun refreshUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.getAllUsers().collect {
                users = it
            }
        }
    }
}