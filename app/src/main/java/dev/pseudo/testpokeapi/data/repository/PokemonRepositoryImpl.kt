package dev.pseudo.testpokeapi.data.repository

import dev.pseudo.testpokeapi.data.model.PokemonInfo
import dev.pseudo.testpokeapi.data.remote.PokeApiService
import dev.pseudo.testpokeapi.domain.model.Pokemon
import dev.pseudo.testpokeapi.domain.repository.PokemonRepository
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
        val response = apiService.getPokemonList(limit, offset)
        return response.results.map { entry ->
            val id = entry.url.trimEnd('/').split("/").last()
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
            Pokemon(
                name = entry.name.replaceFirstChar { it.uppercase() },
                imageUrl = imageUrl
            )
        }
    }

    override suspend fun getPokemonInfo(name: String): PokemonInfo {
        return apiService.getPokemonInfo(name)
    }
}