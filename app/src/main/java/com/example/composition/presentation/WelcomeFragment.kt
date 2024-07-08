package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.composition.R
import com.example.composition.databinding.WelcomeFragmentLayoutBinding

class WelcomeFragment : Fragment() {

    private var _binding: WelcomeFragmentLayoutBinding? = null
    private val binding: WelcomeFragmentLayoutBinding
        get() = _binding ?: throw RuntimeException("WelcomeFragmentLayoutBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WelcomeFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        binding.btnOk.setOnClickListener {
            launchChooseGameLevelFragment()
        }
    }

    private fun launchChooseGameLevelFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(ChooseLevelFragment.NAME)
            .replace(R.id.container, ChooseLevelFragment.newInstance()).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}