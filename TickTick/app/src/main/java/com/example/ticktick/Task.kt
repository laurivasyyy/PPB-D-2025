package com.example.ticktick

data class Task(
    var title: String,
    var deadline: String,
    var isDone: Boolean = false
)
