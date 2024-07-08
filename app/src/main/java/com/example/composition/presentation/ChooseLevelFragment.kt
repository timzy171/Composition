package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.composition.R
import com.example.composition.databinding.ChooseLevelFragmentLayoutBinding
import com.example.composition.domain.entity.Level

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        with(binding) {
            btnTestLevel.setOnClickListener {
                launchGameFragment(Level.TEST)
            }

            btnEasyLevel.setOnClickListener {
                launchGameFragment(Level.EASY)
            }

            btnNormalLevel.setOnClickListener {
                launchGameFragment(Level.NORMAL)
            }

            btnHardLevel.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
        }
    }

    private fun launchGameFragment(level: Level) {
        requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(GameFragment.NAME)
            .replace(R.id.container, GameFragment.newInstance(level)).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val NAME = "ChooseLevelFragment"
        fun newInstance(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }
}