package com.example.os

class ToDoModel {
    companion object Factory {
        fun createList(): ToDoModel = ToDoModel()
    }
    var UID: String? = null
    var ItemDataText: String? = null
    var done: Boolean? = false
}