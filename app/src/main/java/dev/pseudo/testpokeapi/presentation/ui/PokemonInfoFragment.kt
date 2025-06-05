package dev.pseudo.testpokeapi.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import dev.pseudo.testpokeapi.R
import dev.pseudo.testpokeapi.data.model.PokemonInfo
import dev.pseudo.testpokeapi.databinding.FragmentPokemonInfoBinding
import dev.pseudo.testpokeapi.domain.PokemonAttributes
import dev.pseudo.testpokeapi.presentation.viewmodel.InfoViewModel
import dev.pseudo.testpokeapi.presentation.viewmodel.PokemonInfoUiState

@AndroidEntryPoint
class PokemonInfoFragment : Fragment() {

    private lateinit var binding: FragmentPokemonInfoBinding

    private val viewModel: InfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokemonName = arguments?.getString("pokemonName") ?: return
        viewModel.loadPokemonInfo(pokemonName)

        observePokemonInfo()
    }

    private fun observePokemonInfo() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PokemonInfoUiState.Loading -> showLoadingState()
                is PokemonInfoUiState.Success -> showContentState(state.data)
                is PokemonInfoUiState.Error -> showErrorState(state.message)
            }
        }
    }

    private fun showLoadingState() {
        binding.progressBar.visibility = View.VISIBLE
        binding.contentLayout.visibility = View.GONE
        binding.errorText.visibility = View.GONE
    }

    private fun showContentState(pokemonInfo: PokemonInfo) {
        binding.progressBar.visibility = View.GONE
        binding.contentLayout.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE
        fillPokemonData(pokemonInfo)
    }

    private fun showErrorState(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.contentLayout.visibility = View.GONE
        binding.errorText.visibility = View.VISIBLE
        binding.errorText.text = message
    }

    private fun fillPokemonData(pokemonInfo: PokemonInfo) {
        binding.name.text = pokemonInfo.name.replaceFirstChar { it.uppercaseChar() }
        binding.height.text = getString(R.string.height_m, (pokemonInfo.height / 10).toString())
        binding.weight.text = getString(R.string.weight_kg, (pokemonInfo.weight / 10).toString())

        pokemonInfo.types.forEach { typeEntry ->
            val chip = Chip(requireContext()).apply {
                text = typeEntry.type.name.replaceFirstChar { it.uppercaseChar() }
                isClickable = false
                isCheckable = false
                chipBackgroundColor = ContextCompat.getColorStateList(
                    requireContext(),
                    PokemonAttributes.getColorsByType(typeEntry.type.name)
                )
                setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }
            binding.typeGroup.addView(chip)
        }

        val statsMap = pokemonInfo.stats.associate { it.stat.name to it.base_stat }

        binding.hpBar.progress = statsMap["hp"] ?: 0
        binding.attackBar.progress = statsMap["attack"] ?: 0
        binding.defenseBar.progress = statsMap["defense"] ?: 0

        binding.hpValue.text = "${statsMap["hp"] ?: "?"}"
        binding.attackValue.text = "${statsMap["attack"] ?: "?"}"
        binding.defenseValue.text = "${statsMap["defense"] ?: "?"}"

        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemonInfo.id}.png"
        Glide.with(this)
            .load(imageUrl)
            .into(binding.image)
    }
}
