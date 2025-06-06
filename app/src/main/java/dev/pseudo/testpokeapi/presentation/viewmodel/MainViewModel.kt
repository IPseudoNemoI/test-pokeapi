package dev.pseudo.testpokeapi.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.pseudo.testpokeapi.domain.model.Pokemon
import dev.pseudo.testpokeapi.domain.repository.PokemonRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<PokemonListUiState>()
    val uiState: LiveData<PokemonListUiState> = _uiState

    private var offset = 0
    private var isLoading = false
    private var currentList = mutableListOf<Pokemon>()
    private var originalList = mutableListOf<Pokemon>()

    fun loadPokemon(limit: Int = 30) {
        if (isLoading) return
        isLoading = true

        _uiState.value = PokemonListUiState.Loading

        viewModelScope.launch {
            try {
                val newList = repository.getPokemonList(limit, offset)
                offset += limit
                currentList.addAll(newList)
                originalList.addAll(newList)

                _uiState.value = PokemonListUiState.Success(currentList)
            } catch (e: Exception) {
                if (currentList.isEmpty()) {
                    _uiState.value = PokemonListUiState.Error(e.message ?: "No connection")
                } else {
                    _uiState.value = PokemonListUiState.Success(currentList)
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun loadRandomPokemonList(limit: Int = 30, maxPokemonCount: Int = 1250) {
        if (isLoading) return
        isLoading = true

        _uiState.value = PokemonListUiState.Loading

        viewModelScope.launch {
            try {
                val maxOffset = maxPokemonCount - limit
                val randomOffset = Random.nextInt(0, maxOffset.coerceAtLeast(0))

                val newList = repository.getPokemonList(limit, randomOffset)
                offset = randomOffset + limit
                currentList = newList.toMutableList()
                originalList = newList.toMutableList()

                _uiState.value = PokemonListUiState.Success(currentList)
            } catch (e: Exception) {
                _uiState.value =
                    PokemonListUiState.Error(e.message ?: "Failed to load random Pokemon list")
            } finally {
                isLoading = false
            }
        }
    }

    fun sortByStats(byAttack: Boolean, byDefense: Boolean, byHp: Boolean) {
        currentList = if (!byAttack && !byDefense && !byHp) {
            originalList.toMutableList()
        } else {
            currentList.sortedWith(compareByDescending<Pokemon> { pokemon ->
                var score = 0
                if (byAttack) score += pokemon.attack
                if (byDefense) score += pokemon.defense
                if (byHp) score += pokemon.hp
                score
            }).toMutableList()
        }

        _uiState.value = PokemonListUiState.Success(currentList)
    }
}

sealed class PokemonListUiState {
    object Loading : PokemonListUiState()
    data class Success(val data: List<Pokemon>) : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}

