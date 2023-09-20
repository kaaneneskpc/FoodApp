package com.example.foodyapp.ui.fragments.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.foodyapp.R
import com.example.foodyapp.core.model.Result
import com.example.foodyapp.databinding.FragmentInstructionsBinding
import com.example.foodyapp.utils.Constants
import com.example.foodyapp.utils.extensions.parcelable


class InstructionsFragment : Fragment() {

    private lateinit var binding: FragmentInstructionsBinding
    private var myBundle: Result? = null
    private var webSiteUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentsData()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_instructions, container, false)
        binding.lifecycleOwner = this
        initView()
        return binding.root
    }

    private fun getArgumentsData() {
        arguments?.let {
            myBundle = it.parcelable(Constants.RECIPE_RESULT_KEY)
        }
    }

    private fun initView() = with(binding) {
        instructionsWebView.apply {
            webViewClient = object : WebViewClient() {}
            webSiteUrl = myBundle?.sourceUrl
            loadUrl(webSiteUrl.orEmpty())
        }
    }


}