package com.daurenbek.hackdayapp.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.daurenbek.hackdayapp.databinding.FragmentOnboardingBinding
import com.daurenbek.hackdayapp.databinding.FragmentProfessionsBinding
import com.daurenbek.hackdayapp.ui.professions.ProfessionsViewModel
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel

class OnboardingFragment: Fragment() {
    private var _binding: FragmentOnboardingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var imageSlider: ImageSlider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = binding.root

        imageSlider = binding.imageSlider

        val images: MutableList<SlideModel> = mutableListOf<SlideModel>()

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}