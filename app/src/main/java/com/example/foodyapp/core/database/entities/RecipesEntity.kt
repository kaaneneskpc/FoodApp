package com.example.foodyapp.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodyapp.core.model.FoodRecipe
import com.example.foodyapp.utils.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}