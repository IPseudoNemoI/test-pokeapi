package dev.pseudo.testpokeapi.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.pseudo.testpokeapi.R
import dev.pseudo.testpokeapi.domain.model.Pokemon
import java.util.Locale

class PokemonAdapter(
    private var items: List<Pokemon>
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.pokemonImage)
        val nameView: TextView = view.findViewById(R.id.pokemonName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = items[position]
        holder.nameView.text = pokemon.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }

        // Загрузка картинки через Glide (подключи зависимость Glide в build.gradle)
        Glide.with(holder.imageView.context)
            .load(pokemon.imageUrl)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<Pokemon>) {
        items = newItems
        notifyDataSetChanged()
    }
}