package com.example.foodyapp.ui.fragments.recipes.bottomSheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.foodyapp.R
import com.example.foodyapp.databinding.RecipesBottomSheetBinding
import com.example.foodyapp.ui.viewModels.recipes.RecipesViewModel
import com.example.foodyapp.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodyapp.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.Locale

class RecipesBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: RecipesBottomSheetBinding
    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.recipes_bottom_sheet, container, false)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId, binding.dietTypeChipGroup)
        }

        binding.apply {
            mealTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
                val chip = group.findViewById<Chip>(selectedChipId.first())
                val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
                mealTypeChip = selectedMealType
                mealTypeChipId = selectedChipId.first()
            }

            dietTypeChipGroup.setOnCheckedStateChangeListener { group, selectedChipId ->
                val chip = group.findViewById<Chip>(selectedChipId.first())
                val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
                dietTypeChip = selectedDietType
                dietTypeChipId = selectedChipId.first()
            }

            applyBtn.setOnClickListener {
                recipesViewModel.saveMealAndDietTypeTemp(
                    mealTypeChip,
                    mealTypeChipId,
                    dietTypeChip,
                    dietTypeChipId
                )
                val action =
                    RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
                findNavController().navigate(action)
            }
        }
        return binding.root
    }


    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                val targetView = chipGroup.findViewById<Chip>(chipId)
                targetView.isChecked = true
                chipGroup.requestChildFocus(targetView, targetView)
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}
