package dev.pseudo.testpokeapi.domain.repository

import dev.pseudo.testpokeapi.data.model.PokemonInfo
import dev.pseudo.testpokeapi.domain.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon>

    suspend fun getPokemonInfo(name: String): PokemonInfo
}