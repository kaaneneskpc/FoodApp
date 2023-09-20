package com.example.foodyapp.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodyapp.R
import com.example.foodyapp.adapter.RecipesAdapter
import com.example.foodyapp.databinding.FragmentRecipesBinding
import com.example.foodyapp.ui.viewModels.MainViewModel
import com.example.foodyapp.ui.viewModels.recipes.RecipesViewModel
import com.example.foodyapp.utils.KeyboardUtil
import com.example.foodyapp.utils.NetworkListener
import com.example.foodyapp.utils.NetworkResult
import com.example.foodyapp.utils.extensions.gone
import com.example.foodyapp.utils.extensions.observeOnce
import com.example.foodyapp.utils.extensions.visible
import com.example.foodyapp.utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener {
    private val args by navArgs<RecipesFragmentArgs>()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var networkListener: NetworkListener
    private var dataRequested = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipes, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        initObservers()
        initViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMenu()
    }

    private fun initObservers() {
        setupRecyclerView()
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                networkListener = NetworkListener()
                networkListener.checkNetworkAvailability(requireContext())
                    .collect { status ->
                        Log.d("NetworkListener", status.toString())
                        recipesViewModel.apply {
                            networkStatus = status
                            showNetworkStatus()
                        }
                        readDatabase()
                    }
            }
        }
    }

    private fun initViews() = with(binding) {
        floatingActionButton.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            requestApiData()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerview.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        showShimmerEffect()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet || database.isNotEmpty() && dataRequested) {
                    Log.d("RecipesFragment", "readDatabase called!")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    if(!dataRequested) {
                        requestApiData()
                        dataRequested = true
                    }
                }
            }
        }
    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData called!")
        mainViewModel.apply {
            getRecipes(recipesViewModel.applyQueries())
            recipesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        hideShimmerEffect()
                        response.data?.let { mAdapter.setData(it) }
                        recipesViewModel.saveMealAndDietType()
                        hideSwipeRefresh()
                    }

                    is NetworkResult.Error -> {
                        loadDataFromCache()
                        hideShimmerEffect()
                        context?.toast(response.message.toString())
                        hideSwipeRefresh()
                    }

                    is NetworkResult.Loading -> {
                        showShimmerEffect()
                        showSwipeRefresh()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                    hideSwipeRefresh()
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    context?.toast(response.message.toString())
                    hideSwipeRefresh()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                    showSwipeRefresh()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            }
        }
    }

    private fun showShimmerEffect() {
        binding.apply {
            shimmerFrameLayout.startShimmer()
            shimmerFrameLayout.visible()
            recyclerview.gone()
        }
    }

    private fun hideShimmerEffect() {
        binding.apply {
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.gone()
            recyclerview.visible()
        }
    }

    private fun showSwipeRefresh() = with(binding) {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun hideSwipeRefresh() = with(binding) {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun setMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.recipes_menu, menu)
                val search = menu.findItem(R.id.menu_search)
                val searchView = search.actionView as? SearchView
                searchView?.apply {
                    isSubmitButtonEnabled = true
                    setOnQueryTextListener(this@RecipesFragment)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { searchApiData(query) }
        KeyboardUtil(binding.root).closeSoftKeyboard()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrBlank()) {
            KeyboardUtil(binding.root).closeSoftKeyboard()
            requestApiData()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}