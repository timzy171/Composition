package com.example.composition.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.GameFinishedFragmentLayoutBinding
import com.example.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()

    private lateinit var gameResult: GameResult


    private var _binding: GameFinishedFragmentLayoutBinding? = null
    private val binding: GameFinishedFragmentLayoutBinding
        get() = _binding ?: throw RuntimeException("GameFinishedFragmentLayoutBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GameFinishedFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameResult = args.gameResult
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupUi() {
        with(binding) {
            tvRequiredAnswers.text = getString(
                R.string.required_score,
                gameResult.gameSettings.minCountOfRightAnswers.toString()
            )

            tvScoreAnswers.text =
                getString(R.string.score_answers, gameResult.countOfRightAnswers.toString())

            tvRequiredPercentage.text = getString(
                R.string.required_percentage,
                gameResult.gameSettings.minPercentOfRightAnswers.toString()
            )

            tvScorePercentage.text = getString(
                R.string.score_percentage,
                (gameResult.countOfRightAnswers.toDouble() / gameResult.countOfQuestions.toDouble() * 100).toInt()
                    .toString()
            )

            if (gameResult.winner) {
                emojiResult.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_smile
                    )
                )
            } else {
                emojiResult.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_loose
                    )
                )
            }
            buttonRetry.setOnClickListener {
                retryGame()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    companion object {
        const val KEY_GAME_RESULT = "game_result"
        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }

}