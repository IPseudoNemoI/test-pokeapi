package dev.pseudo.testpokeapi.data.model

data class PokemonInfo(
    val id: Int,
    val name: String,
    val height: Float,
    val weight: Float,
    val types: List<TypeWrapper>,
    val stats: List<StatWrapper>,
) {
    val typeNames: List<String> get() = types.map { it.type.name }
    val parsedStats: ParsedStats
        get() {
            val map = stats.associateBy { it.stat.name }
            return ParsedStats(
                attack = map["attack"]?.base_stat ?: 0,
                defense = map["defense"]?.base_stat ?: 0,
                hp = map["hp"]?.base_stat ?: 0
            )
        }
}

data class TypeWrapper(val type: Type)
data class Type(val name: String)

data class StatWrapper(val base_stat: Int, val stat: Stat)
data class Stat(val name: String)

data class ParsedStats(val attack: Int, val defense: Int, val hp: Int)
