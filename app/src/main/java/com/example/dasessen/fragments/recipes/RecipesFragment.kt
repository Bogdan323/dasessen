package com.example.cupcake.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cupcake.viewmodels.MainViewModel
import com.example.cupcake.R
import com.example.cupcake.fragments.recipes.adapters.RecipesAdapter
import com.example.cupcake.databinding.FragmentRecipesBinding
import com.example.cupcake.util.NetworkListener
import com.example.cupcake.util.NetworkResult
import com.example.cupcake.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private val binding by viewBinding(FragmentRecipesBinding::bind)
    private val mainViewModel : MainViewModel by activityViewModels()
    private var recipesAdapter: RecipesAdapter? = null

    //Network listener
    private lateinit var networkListener: NetworkListener

    //args
    private val args by navArgs<RecipesFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipesAdapter = RecipesAdapter()
        binding.mainViewModel = mainViewModel

        setupRecyclerView()
        subscribeLocal()

        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    mainViewModel.networkStatus = status
                }
        }

        binding.recipesFilter.setOnClickListener {
            if (mainViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                mainViewModel.showNetworkStatus()
            }
        }
    }

    private fun subscribeLocal() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Log.d("Recipes Fragment", "readDatabase called!")
                    database[0].foodRecipe.let { recipesAdapter?.submitList(it.results) }
                    hideShimmerEffect()
                } else {
                    subscribeUi()
                }
            }
        }
    }

    private fun subscribeUi() {
        Log.d("Recipes Fragment", "requestApi called!")
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Log.d("Recipes Fragment", "requestApi SUCCESS!")
                    hideShimmerEffect()
                    response.data?.let { recipesAdapter?.submitList(it.results) }
                }
                is NetworkResult.Error -> {
                    Log.d("Recipes Fragment", "requestApi ERROR!")
                    hideShimmerEffect()
                    subscribeFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Log.d("Recipes Fragment", "requestApi LOADING!")
                    showShimmerEffect()
                }
                else -> {}
            }
        }
    }

    private fun subscribeFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) {database->
                if (database.isNotEmpty()) {
                    database[0].foodRecipe.let { recipesAdapter?.submitList(it.results) }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = recipesAdapter
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.shimmerrFrameLayout.startShimmer()
        binding.shimmerrFrameLayout.visibility = View.VISIBLE
    }

    private fun hideShimmerEffect() {
        binding.shimmerrFrameLayout.hideShimmer()
        binding.shimmerrFrameLayout.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recipesAdapter = null
    }

}