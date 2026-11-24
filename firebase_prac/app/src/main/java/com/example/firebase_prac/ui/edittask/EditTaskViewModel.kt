package com.example.firebase_prac.ui.edittask

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_prac.model.Task
import com.example.firebase_prac.model.service.StorageService
import com.example.firebase_prac.model.service.impl.StorageServiceImpl
import kotlinx.coroutines.launch

class EditTaskViewModel(
    private val storageService: StorageService = StorageServiceImpl()
) : ViewModel() {
    var task = mutableStateOf(Task())
        private set

    fun initialize(taskId: String) {
        if (taskId.isNotBlank()) {
            viewModelScope.launch {
                storageService.getTask(taskId)?.let { loadedTask ->
                    task.value = loadedTask
                }
            }
        }
    }

    fun onTitleChange(newValue: String) {
        task.value = task.value.copy(title = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        task.value = task.value.copy(description = newValue)
    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        viewModelScope.launch {
            val editedTask = task.value
            if (editedTask.id.isBlank()) {
                storageService.save(editedTask)
            } else {
                storageService.update(editedTask)
            }
            popUpScreen()
        }
    }
}
