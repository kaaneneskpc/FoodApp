package com.example.foodyapp.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodyapp.R
import com.example.foodyapp.adapter.IngredientsAdapter
import com.example.foodyapp.core.model.Result
import com.example.foodyapp.databinding.FragmentIngredientsBinding
import com.example.foodyapp.utils.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foodyapp.utils.extensions.parcelable


class IngredientsFragment : Fragment() {
    private lateinit var binding: FragmentIngredientsBinding
    private var myBundle: Result? = null
    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentsData()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false)
        binding.lifecycleOwner = this
        initArgs()
        initRecyclerView()
        return binding.root
    }

    private fun getArgumentsData() {
        arguments?.let {
            myBundle = it.parcelable(RECIPE_RESULT_KEY)
        }
    }

    private fun initArgs() {
        myBundle?.extendedIngredients?.let { ingredientsAdapter.setData(it) }
    }

    private fun initRecyclerView() = with(binding) {
        ingredientsRecyclerview.apply {
            adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}