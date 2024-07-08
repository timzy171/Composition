package com.example.composition.presentation

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R
import com.example.composition.databinding.GameFragmentLayoutBinding
import com.example.composition.domain.entity.GameResult

class GameFragment : Fragment() {

    private lateinit var level: com.example.composition.domain.entity.Level

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[(GameViewModel::class.java)]
    }

    private var _binding: GameFragmentLayoutBinding? = null
    private val binding: GameFragmentLayoutBinding
        get() = _binding ?: throw RuntimeException("GameFragmentLayoutBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    private fun observeViewModel() {
        with(viewModel) {
            timer.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }

            gameFinished.observe(viewLifecycleOwner) {
                viewModel.gameResult.observe(viewLifecycleOwner) {
                    launchGameFinishedFragment(it)
                }
            }

            question.observe(viewLifecycleOwner) {
                binding.tvSumItem.text = it.sum.toString()
                binding.tvVisibleItem.text = it.visibleNumber.toString()
                setOptions(it.options)
            }

            percentOfRightAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it.toInt(), true)
            }

            rightAnswersStr.observe(viewLifecycleOwner) {
                binding.tvRightAnswers.text = it
            }

            enoughRightAnswers.observe(viewLifecycleOwner) {
                if (it) {
                    binding.tvRightAnswers.setTextColor(requireActivity().getColor(R.color.enough_right_answers))
                } else {
                    binding.tvRightAnswers.setTextColor(requireActivity().getColor(R.color.not_enough_right_answers))
                }
            }

            minPercentOfRightAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.secondaryProgress = it
            }

            enoughPercentOfRightAnswers.observe(viewLifecycleOwner) {
                if (it) {
                    binding.progressBar.progressTintList =
                        ColorStateList.valueOf(requireActivity().getColor(R.color.enough_right_answers))
                } else {
                    binding.progressBar.progressTintList =
                        ColorStateList.valueOf(requireActivity().getColor(R.color.not_enough_right_answers))
                }
            }
        }

    }

    private fun setOptions(options: List<Int>) {
        with(binding) {
            val tvOptionsList =
                listOf(tvOption1, tvOption2, tvOption3, tvOption4, tvOption5, tvOption6)

            for (index in tvOptionsList.indices) {
                tvOptionsList[index].text = options[index].toString()
            }
        }

        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            tvOption1.setOnClickListener {
                viewModel.makeAnswer(tvOption1.text.toString().toInt())
            }
            tvOption2.setOnClickListener {
                viewModel.makeAnswer(tvOption2.text.toString().toInt())
            }
            tvOption3.setOnClickListener {
                viewModel.makeAnswer(tvOption3.text.toString().toInt())
            }
            tvOption4.setOnClickListener {
                viewModel.makeAnswer(tvOption4.text.toString().toInt())
            }
            tvOption5.setOnClickListener {
                viewModel.makeAnswer(tvOption5.text.toString().toInt())
            }
            tvOption6.setOnClickListener {
                viewModel.makeAnswer(tvOption6.text.toString().toInt())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.startGame(level)
    }


    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.container, GameFinishedFragment.newInstance(gameResult)).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        level =
            requireArguments().getSerializable(KEY_LEVEL) as com.example.composition.domain.entity.Level
    }

    companion object {
        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "level"
        fun newInstance(level: com.example.composition.domain.entity.Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_LEVEL, level)
                }
            }
        }
    }
}