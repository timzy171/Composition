package com.example.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composition.R
import com.example.composition.data.GameRepositoryImpl
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.usecases.GenerateQuestionUseCase
import com.example.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(private var application: Application) : AndroidViewModel(application) {
    private val repository = GameRepositoryImpl
    private var generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private var getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private lateinit var level: Level
    private lateinit var gameSettings: GameSettings

    private var answersCount = 0.0
    private var rightAnswersCount = 0.0

    private var _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private var _timer = MutableLiveData<String>()
    val timer: LiveData<String>
        get() = _timer

    private var _rightAnswersStr = MutableLiveData<String>()
    val rightAnswersStr: LiveData<String>
        get() = _rightAnswersStr

    private var _minPercentOfRightAnswers = MutableLiveData<Int>()
    val minPercentOfRightAnswers: LiveData<Int>
        get() = _minPercentOfRightAnswers

    private var _percentOfRightAnswers = MutableLiveData<Double>()
    val percentOfRightAnswers: LiveData<Double>
        get() = _percentOfRightAnswers

    private var _gameFinished = MutableLiveData<Unit>()
    val gameFinished: LiveData<Unit>
        get() = _gameFinished

    private var _enoughRightAnswers = MutableLiveData<Boolean>()
    val enoughRightAnswers: LiveData<Boolean>
        get() = _enoughRightAnswers

    private var _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    private var _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private var gameTimer: CountDownTimer? = null


    fun startGame(level: Level) {
        this.level = level
        gameSettings = getGameSettingsUseCase.invoke(this.level)
        _rightAnswersStr.value = application.getString(
            R.string.right_answers,
            rightAnswersCount.toInt().toString(),
            gameSettings.minCountOfRightAnswers.toString()
        )

        _minPercentOfRightAnswers.value = gameSettings.minPercentOfRightAnswers
        generateNewQuestion()
        initTimer(gameSettings)
    }

    private fun generateNewQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    fun makeAnswer(answer: Int) {
        ++answersCount
        if (answer + _question.value?.visibleNumber!! == _question.value?.sum) {
            ++rightAnswersCount
            _rightAnswersStr.value = application.getString(
                R.string.right_answers,
                rightAnswersCount.toInt().toString(),
                gameSettings.minCountOfRightAnswers.toString()
            )
            generateNewQuestion()
        }
        calculateRightAnswersPercent()
    }


    private fun calculateRightAnswersPercent() {
        _percentOfRightAnswers.value = rightAnswersCount / answersCount * HUNDRED_PERCENT
        _enoughPercentOfRightAnswers.value =
            _percentOfRightAnswers.value!! >= gameSettings.minPercentOfRightAnswers
        _enoughRightAnswers.value = rightAnswersCount >= gameSettings.minCountOfRightAnswers

    }

    private fun initTimer(gameSettings: GameSettings) {
        gameTimer = object :
            CountDownTimer(gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS, MILLIS_IN_SECONDS) {
            override fun onTick(millisUntilFinished: Long) {
                _timer.value = convertSecondsToString(millisUntilFinished / MILLIS_IN_SECONDS)
            }

            override fun onFinish() {
                finishGame()
            }

        }

        gameTimer?.start()
    }

    private fun finishGame() {
        val winner = _enoughRightAnswers.value!! && _enoughPercentOfRightAnswers.value!!
        _gameResult.value =
            GameResult(winner, rightAnswersCount.toInt(), answersCount.toInt(), gameSettings)
        _gameFinished.value = Unit
    }

    private fun convertSecondsToString(gameSettingsSeconds: Long): String {
        val minutes = gameSettingsSeconds / SECONDS_IN_MINUTE
        val seconds = gameSettingsSeconds - (SECONDS_IN_MINUTE * minutes.toInt())
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        gameTimer?.cancel()
    }

    private companion object {
        private const val SECONDS_IN_MINUTE = 60
        private const val MILLIS_IN_SECONDS = 1000L
        private const val HUNDRED_PERCENT = 100
    }
}