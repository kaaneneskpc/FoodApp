package com.example.foodyapp.adapter.bindingAdapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foodyapp.core.database.entities.FoodJokeEntity
import com.example.foodyapp.core.model.FoodJoke
import com.example.foodyapp.utils.NetworkResult
import com.example.foodyapp.utils.extensions.gone
import com.example.foodyapp.utils.extensions.invisible
import com.example.foodyapp.utils.extensions.visible
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {

    companion object {

        @BindingAdapter("readApiResponse3", "readDatabase3", requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visible()
                        }
                        is MaterialCardView -> {
                            view.invisible()
                        }
                    }
                }
                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> {
                            view.invisible()
                        }
                        is MaterialCardView -> {
                            view.visible()
                            if (database != null) {
                                if (database.isEmpty()) {
                                    view.invisible()
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when(view){
                        is ProgressBar -> {
                            view.invisible()
                        }
                        is MaterialCardView -> {
                            view.visible()
                        }
                    }
                }

                else -> {}
            }
        }

        @BindingAdapter("readApiResponse4", "readDatabase4", requireAll = true)
        @JvmStatic
        fun setErrorViewsVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ){
            if(database != null){
                if(database.isEmpty()){
                    view.visible()
                    if(view is TextView){
                        if(apiResponse != null) {
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
            }
            if(apiResponse is NetworkResult.Success){
                view.invisible()
            }
        }

    }

}