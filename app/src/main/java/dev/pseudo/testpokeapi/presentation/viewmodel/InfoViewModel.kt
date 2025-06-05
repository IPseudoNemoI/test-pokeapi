package dev.pseudo.testpokeapi.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.pseudo.testpokeapi.data.model.PokemonInfo
import dev.pseudo.testpokeapi.domain.repository.PokemonRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<PokemonInfoUiState>()
    val uiState: LiveData<PokemonInfoUiState> = _uiState

    fun loadPokemonInfo(name: String) {
        _uiState.value = PokemonInfoUiState.Loading

        viewModelScope.launch {
            try {
                val detail = repository.getPokemonInfo(name)
                _uiState.value = PokemonInfoUiState.Success(detail)
            } catch (e: Exception) {
                _uiState.value = PokemonInfoUiState.Error("No connection")
            }
        }
    }
}

sealed class PokemonInfoUiState {
    object Loading : PokemonInfoUiState()
    data class Success(val data: PokemonInfo) : PokemonInfoUiState()
    data class Error(val message: String) : PokemonInfoUiState()
}
