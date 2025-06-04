package dev.pseudo.testpokeapi.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.pseudo.testpokeapi.databinding.FragmentPokemonListBinding
import dev.pseudo.testpokeapi.presentation.adapter.PokemonAdapter
import dev.pseudo.testpokeapi.presentation.viewmodel.MainViewModel

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private lateinit var binding: FragmentPokemonListBinding

    private lateinit var adapter: PokemonAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PokemonAdapter(emptyList())
        binding.pokemonRecyclerView.adapter = adapter

        viewModel.pokemonList.observe(viewLifecycleOwner) { list ->
            Log.d("deblog", "$list")
            adapter.updateList(list)
        }

        viewModel.loadPokemon()
    }

}
