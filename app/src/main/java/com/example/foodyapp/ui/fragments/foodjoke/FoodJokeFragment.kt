package com.example.foodyapp.ui.fragments.foodjoke

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.foodyapp.R
import com.example.foodyapp.databinding.FragmentFoodJokeBinding
import com.example.foodyapp.ui.viewModels.MainViewModel
import com.example.foodyapp.utils.Constants.Companion.API_KEY
import com.example.foodyapp.utils.NetworkResult
import kotlinx.coroutines.launch


class FoodJokeFragment : Fragment() {
    private lateinit var binding: FragmentFoodJokeBinding
    private lateinit var mainViewModel: MainViewModel
    private var foodJoke = "No Food Joke"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_joke, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        initObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMenu()
    }

    private fun initObservers() = with(binding) {
        mainViewModel?.apply {
            getFoodJoke(API_KEY)
            foodJokeResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        foodJokeTextView.text = response.data?.text.orEmpty()
                        if (response.data != null) {
                            foodJoke = response.data.text
                        }
                    }

                    is NetworkResult.Error -> {
                        loadDataFromCache()
                        Toast.makeText(
                            requireContext(),
                            response.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is NetworkResult.Loading -> {
                        Log.d("FoodJokeFragment", "Loading")
                    }

                    else -> {}
                }
            }
        }
    }

    private fun setMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.food_joke_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.share_food_joke_menu -> {
                        shareIntent()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun shareIntent() {
        val shareIntent = Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, foodJoke)
            this.type = "text/plain"
        }
        startActivity(shareIntent)
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner) { database ->
                if (!database.isNullOrEmpty()) {
                    binding.foodJokeTextView.text = database[0].foodJoke.text
                    foodJoke = database[0].foodJoke.text
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }


}