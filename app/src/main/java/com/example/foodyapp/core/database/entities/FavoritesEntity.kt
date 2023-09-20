package com.example.foodyapp.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodyapp.utils.Constants.Companion.FAVORITE_RECIPES_TABLE
import com.example.foodyapp.core.model.Result

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)