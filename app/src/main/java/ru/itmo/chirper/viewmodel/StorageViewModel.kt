package ru.itmo.chirper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.itmo.chirper.domain.usecase.GetStorageDataUseCase

class StorageViewModel(
    private val getUseCase: GetStorageDataUseCase,
) : ViewModel() {

    private val _storageState = MutableStateFlow<Result<Map<String, Any>>?>(null)
    val storageState: StateFlow<Result<Map<String, Any>>?> = _storageState

    fun getData(key: String) {
        viewModelScope.launch {
            try {
                val data = getUseCase.execute(key)
                _storageState.value = Result.success(data)
            } catch (e: Exception) {
                _storageState.value = Result.failure(e)
            }
        }
    }
}
