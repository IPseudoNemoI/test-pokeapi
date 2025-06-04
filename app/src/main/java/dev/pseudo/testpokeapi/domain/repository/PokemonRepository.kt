package dev.pseudo.testpokeapi.domain.repository

import dev.pseudo.testpokeapi.domain.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon>
}