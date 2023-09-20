package com.example.foodyapp.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import coil.load
import com.example.foodyapp.R
import com.example.foodyapp.adapter.bindingAdapters.RecipesRowBinding
import com.example.foodyapp.databinding.FragmentOverviewBinding
import com.example.foodyapp.core.model.Result
import com.example.foodyapp.utils.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foodyapp.utils.extensions.parcelable
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding
    private var myBundle: Result? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentsData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overview, container, false)
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }

    private fun getArgumentsData() {
        arguments?.let {
            myBundle = it.parcelable(RECIPE_RESULT_KEY)
        }
    }

    private fun initView() = with(binding) {
        with(myBundle) {
            mainImageView.load(this?.image)
            titleTextView.text = this?.title
            likesTextView.text = this?.aggregateLikes.toString()
            timeTextView.text = this?.readyInMinutes.toString()
            RecipesRowBinding.parseHtml(summaryTextView, this?.summary)
            checkTextAndImageForBundle()
        }
    }

    private fun checkTextAndImageForBundle() = with(binding) {
        myBundle?.apply {
            vegetarian?.let { updateColors(it, vegetarianTextView, vegetarianImageView) }
            vegan?.let { updateColors(it, veganTextView, veganImageView) }
            cheap?.let { updateColors(it, cheapTextView, cheapImageView) }
            dairyFree?.let { updateColors(it, dairyFreeTextView, dairyFreeImageView) }
            glutenFree?.let { updateColors(it, glutenFreeTextView, glutenFreeImageView) }
            veryHealthy?.let { updateColors(it, healthyTextView, healthyImageView) }
        }
    }

    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }


}