package com.example.cupcake.fragments.recipes.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.cupcake.R
import com.example.cupcake.databinding.FragmentDetailBinding
import com.example.cupcake.util.viewBinding


class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)

    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = args.recipe

        binding.apply {
            recipeImageView.load(recipe.image)
            titleTv.text = recipe.title
            likesTextView.text = recipe.aggregateLikes.toString()
            timeTextView.text = recipe.readyInMinutes.toString()
            summaryTextView.text = recipe.summary
        }
    }

}