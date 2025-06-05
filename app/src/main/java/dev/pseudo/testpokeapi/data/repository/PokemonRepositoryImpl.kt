package dev.pseudo.testpokeapi.data.repository

import dev.pseudo.testpokeapi.data.model.PokemonInfo
import dev.pseudo.testpokeapi.data.remote.PokeApiService
import dev.pseudo.testpokeapi.domain.model.Pokemon
import dev.pseudo.testpokeapi.domain.repository.PokemonRepository
import javax.inject.Inject
import kotlin.random.Random

class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService
) : PokemonRepository {

    companion object {
        private const val MAX_POKEMON_COUNT = 1250
    }

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

    override suspend fun getRandomPokemonList(minLimit: Int): List<Pokemon> {
        val safeLimit = minLimit.coerceAtLeast(30)
        val maxOffset = MAX_POKEMON_COUNT - safeLimit
        val randomOffset = Random.nextInt(0, maxOffset)
        return getPokemonList(limit = safeLimit, offset = randomOffset)
    }
}