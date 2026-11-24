package com.example.firebase_prac.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_prac.model.Task
import com.example.firebase_prac.model.service.ConfigurationService
import com.example.firebase_prac.model.service.StorageService
import com.example.firebase_prac.model.service.impl.ConfigurationServiceImpl
import com.example.firebase_prac.model.service.impl.StorageServiceImpl
import com.example.firebase_prac.ui.EDIT_TASK_SCREEN
import com.example.firebase_prac.ui.SETTINGS_SCREEN
import com.example.firebase_prac.ui.TASK_ID
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TasksViewModel(
    private val storageService: StorageService = StorageServiceImpl(),
    private val configurationService: ConfigurationService = ConfigurationServiceImpl()
) : ViewModel() {

    val tasks: StateFlow<List<Task>> = storageService.tasks
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val showEditButton: Boolean
        get() = configurationService.isShowTaskEditButtonConfig

    fun onAddClick(openScreen: (String) -> Unit) {
        openScreen("$EDIT_TASK_SCREEN?$TASK_ID=")
    }

    fun onSettingsClick(openScreen: (String) -> Unit) {
        openScreen(SETTINGS_SCREEN)
    }

    fun onTaskCheckChange(task: Task) {
        viewModelScope.launch {
            storageService.update(task.copy(completed = !task.completed))
        }
    }

    fun onTaskActionClick(openScreen: (String) -> Unit, task: Task) {
        openScreen("$EDIT_TASK_SCREEN?$TASK_ID=${task.id}")
    }

    fun onDeleteTaskClick(task: Task) {
        viewModelScope.launch {
            storageService.delete(task.id)
        }
    }
}
