package ru.itmo.chirper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.itmo.chirper.domain.usecase.GetSduiStorageDataUseCase
import ru.itmo.chirper.state.ChirperState

class SduiProfileViewModel(
    private val getUseCase: GetSduiStorageDataUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ChirperState>(ChirperState.Loading)
    val state: StateFlow<ChirperState> = _state

    fun getData(key: String) {
        viewModelScope.launch {
            _state.value = ChirperState.Loading
            try {
                val data = getUseCase.execute(key)
                _state.value = ChirperState.Success(storage = data)
            } catch (e: Exception) {
                _state.value = ChirperState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

