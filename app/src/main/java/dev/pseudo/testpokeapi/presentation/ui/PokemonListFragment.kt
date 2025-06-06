package dev.pseudo.testpokeapi.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.pseudo.testpokeapi.R
import dev.pseudo.testpokeapi.databinding.FragmentPokemonListBinding
import dev.pseudo.testpokeapi.presentation.adapter.PokemonAdapter
import dev.pseudo.testpokeapi.presentation.viewmodel.MainViewModel
import dev.pseudo.testpokeapi.presentation.viewmodel.PokemonListUiState

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

        initPokemonAdapter()
        observeUiState()
        setListeners()

        viewModel.loadPokemon()
    }

    private fun initPokemonAdapter() {
        adapter = PokemonAdapter(emptyList()) { selectedPokemon ->
            findNavController().navigate(
                R.id.action_pokemonListFragment_to_pokemonInfoFragment,
                bundleOf("pokemonName" to selectedPokemon.name)
            )
        }
        binding.pokemonRecyclerView.adapter = adapter
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PokemonListUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.errorText.visibility = View.GONE
                }

                is PokemonListUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorText.visibility = View.GONE
                    binding.pokemonRecyclerView.visibility = View.VISIBLE
                    adapter.updateList(state.data)
                }

                is PokemonListUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    if (adapter.itemCount == 0) {
                        binding.errorText.visibility = View.VISIBLE
                        binding.pokemonRecyclerView.visibility = View.GONE
                        binding.errorText.text = state.message
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.pokemonRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount <= lastVisibleItem + 5) {
                        viewModel.loadPokemon()
                    }
                }
            }
        )

        binding.reloadButton.setOnClickListener {
            viewModel.loadRandomPokemonList()
        }

        binding.attackCheckBox.setOnCheckedChangeListener { _, _ -> sort() }
        binding.defenseCheckBox.setOnCheckedChangeListener { _, _ -> sort() }
        binding.hpCheckBox.setOnCheckedChangeListener { _, _ -> sort() }
    }

    private fun sort() {
        viewModel.sortByStats(
            binding.attackCheckBox.isChecked,
            binding.defenseCheckBox.isChecked,
            binding.hpCheckBox.isChecked
        )
    }
}

