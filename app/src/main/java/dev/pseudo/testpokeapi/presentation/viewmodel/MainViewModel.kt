package dev.pseudo.testpokeapi.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.pseudo.testpokeapi.data.model.PokemonEntry
import dev.pseudo.testpokeapi.domain.model.Pokemon
import dev.pseudo.testpokeapi.domain.repository.PokemonRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableLiveData<List<Pokemon>>() // ← поправлено здесь
    val pokemonList: LiveData<List<Pokemon>> = _pokemonList

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadPokemon(limit: Int = 30, offset: Int = 0) {
        viewModelScope.launch {
            try {
                val list = repository.getPokemonList(limit, offset)

                Log.d("POKEMON_API", "Loaded ${list.size} Pokémon:")
                list.forEach { Log.d("POKEMON_API", "${it.name} — ${it.imageUrl}") }

                _pokemonList.value = list
            } catch (e: Exception) {
                Log.e("POKEMON_API", "Error loading Pokémon", e)
                _error.value = e.message
            }
        }
    }
}