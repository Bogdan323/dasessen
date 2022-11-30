package com.example.cupcake.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cupcake.models.FoodRecipe
import com.example.cupcake.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

}