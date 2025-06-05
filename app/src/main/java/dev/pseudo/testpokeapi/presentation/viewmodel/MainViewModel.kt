package dev.pseudo.testpokeapi.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.pseudo.testpokeapi.domain.model.Pokemon
import dev.pseudo.testpokeapi.domain.repository.PokemonRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableLiveData<List<Pokemon>>(emptyList())
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var offset = 0
    private var isLoading = false

    fun loadPokemon(limit: Int = 30) {
        if (isLoading) return
        isLoading = true

        viewModelScope.launch {
            try {
                val newList = repository.getPokemonList(limit, offset)
                offset += limit
                val currentList = _pokemonList.value ?: emptyList()
                _pokemonList.value = currentList + newList
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                isLoading = false
            }
        }
    }
}
