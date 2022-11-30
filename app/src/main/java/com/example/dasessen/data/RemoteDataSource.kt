package com.example.cupcake.data

import com.example.cupcake.data.network.FoodRecipesApi
import com.example.cupcake.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
       return foodRecipesApi.getRecipes(queries)
    }

}