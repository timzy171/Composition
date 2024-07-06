package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.composition.databinding.ChooseLevelFragmentLayoutBinding

class ChooseLevelFragment : Fragment() {

    private var _binding: ChooseLevelFragmentLayoutBinding? = null
    private val binding: ChooseLevelFragmentLayoutBinding
        get() = _binding ?: throw RuntimeException("ChooseLevelFragmentLayoutBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChooseLevelFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}