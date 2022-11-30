package com.example.cupcake.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.cupcake.data.database.RecipesEntity
import com.example.cupcake.models.FoodRecipe
import com.example.cupcake.util.NetworkResult

class RecipesBinding {

    companion object {

        @BindingAdapter("readApiResponse","readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            when(apiResponse) {
                is NetworkResult.Error -> {
                    if (database.isNullOrEmpty()) imageView.visibility = View.VISIBLE
                }
                is NetworkResult.Loading -> {
                    imageView.visibility = View.INVISIBLE
                }
                is NetworkResult.Success -> {
                    imageView.visibility = View.INVISIBLE
                }
                else -> {imageView.visibility = View.INVISIBLE}
            }
        }

        @BindingAdapter("readApiResponse2","readDatabase2", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            when(apiResponse) {
                is NetworkResult.Error -> {
                    if (database.isNullOrEmpty()) textView.visibility = View.VISIBLE
                }
                is NetworkResult.Loading -> {
                    textView.visibility = View.INVISIBLE
                }
                is NetworkResult.Success -> {
                    textView.visibility = View.INVISIBLE
                }
                else ->{textView.visibility = View.INVISIBLE}
            }
        }

    }

}