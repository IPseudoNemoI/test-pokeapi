package dev.pseudo.testpokeapi.data.model

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonEntry>,
)