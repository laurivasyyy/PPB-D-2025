package com.example.ticktick.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ticktick.data.TaskDatabase
import com.example.ticktick.data.TaskEntity
import com.example.ticktick.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val allTasksByDeadline: LiveData<List<TaskEntity>>
    val allTasksByStatus: LiveData<List<TaskEntity>>

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        allTasksByDeadline = repository.allTasksByDeadline
        allTasksByStatus = repository.allTasksByStatus
    }

    fun getOverdueTasks(): LiveData<List<TaskEntity>> {
        return repository.getOverdueTasks()
    }

    fun insert(task: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }

    fun update(task: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(task)
    }

    fun delete(task: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(task)
    }

    fun toggleTaskStatus(task: TaskEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.toggleTaskStatus(task)
    }
}