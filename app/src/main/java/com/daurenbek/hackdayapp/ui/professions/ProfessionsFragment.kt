package com.daurenbek.hackdayapp.ui.professions

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.daurenbek.hackdayapp.R
import com.daurenbek.hackdayapp.ResourceHelper
import com.daurenbek.hackdayapp.databinding.FragmentProfessionsBinding
import com.daurenbek.hackdayapp.network.SpecializationModel
import com.daurenbek.hackdayapp.network.SubjectModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfessionsFragment : Fragment() {

    private var _binding: FragmentProfessionsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfessionsViewModel> {
        ProfessionsViewModelFactory(
            ResourceHelper(
                requireContext()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getAllSpecializations()

        _binding = FragmentProfessionsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.apply {
            autoCompleteSubject.setOnItemClickListener { parent, view, position, id ->
                binding.autoCompleteSubject.setTextColor(Color.BLACK)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.professionEvents.collect {
                when (it) {
                    is ProfessionsViewModel.ProfessionEvents.ShowAllResult -> {
                        showAllSpecializations(it.result)
                    }
                    is ProfessionsViewModel.ProfessionEvents.ShowSubjectsResult -> {
                        showAllSubjects(it.result)
                    }
                }
            }
        }

        return root
    }

    private fun showAllSpecializations(allSpecializationsList: List<SpecializationModel>?) {
        binding.apply {
            if (allSpecializationsList?.isNotEmpty() == true) {
                specializationsRecyclerView.adapter = SpecializationItemAdapter(this@ProfessionsFragment, allSpecializationsList)

            }
        }
    }

    private fun showAllSubjects(allSubjectsList: List<SubjectModel>?) {
        binding.apply {
            if (allSubjectsList?.isNotEmpty() == true) {
                val subjects: MutableList<String> = mutableListOf()
                for(subject in allSubjectsList) {
                    subjects.add(subject.name)
                }
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_subject, subjects)
//        arrayAdapter.getDropDownView(methods.size - 1, binding.autoCompletePhoneNumber, binding.dropdownSearch).backgroundTintList = ResourcesCompat.getColorStateList(resources, R.color.white, null)
                binding.autoCompleteSubject.setAdapter(arrayAdapter)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getAllSubjects()

//        recyclerViewState?.let {
//            binding.resultRecycler.getLayoutManager()?.onRestoreInstanceState(it)
//            binding.apply {
//                resultText.visibility = View.VISIBLE
//                containerResult.visibility = View.GONE
//                hintResult.visibility = View.VISIBLE
//            }
//            recyclerViewState = null // to prevent state restoration on list updates
//        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
