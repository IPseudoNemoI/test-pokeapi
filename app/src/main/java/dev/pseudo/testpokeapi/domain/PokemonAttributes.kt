package dev.pseudo.testpokeapi.domain

import dev.pseudo.testpokeapi.R

object PokemonAttributes {

    fun getColorsByType(type: String): Int = when (type.lowercase()) {
        "normal" -> R.color.normal
        "fire" -> R.color.fire
        "water" -> R.color.water
        "electric" -> R.color.electric
        "grass" -> R.color.grass
        "ice" -> R.color.ice
        "fighting" -> R.color.fighting
        "poison" -> R.color.poison
        "ground" -> R.color.ground
        "flying" -> R.color.flying
        "psychic" -> R.color.psychic
        "bug" -> R.color.bug
        "rock" -> R.color.rock
        "ghost" -> R.color.ghost
        "dragon" -> R.color.dragon
        "dark" -> R.color.dark
        "steel" -> R.color.steel
        "fairy" -> R.color.fairy
        else -> R.color.default_type
    }
}