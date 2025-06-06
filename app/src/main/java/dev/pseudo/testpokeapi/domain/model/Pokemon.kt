package dev.pseudo.testpokeapi.domain.model

data class Pokemon(
    val name: String,
    val imageUrl: String,
    val attack: Int = 0,
    val defense: Int = 0,
    val hp: Int = 0,
)