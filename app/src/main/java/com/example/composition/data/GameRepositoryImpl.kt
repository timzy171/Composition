package com.example.composition.data

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_ANSWER_NUMBER = 1
    private const val MIN_SUM_VALUE = 2

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_NUMBER, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_NUMBER)
        val to = min(maxSumValue, rightAnswer + countOfOptions)

        while(options.size != countOfOptions) {
            options.add(Random.nextInt(from, to))
        }

        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(gameLevel: Level): GameSettings {
        return when (gameLevel) {
            Level.TEST -> {
                GameSettings(10,3,50,8)
            }
            Level.EASY -> {
                GameSettings(10,10,70,60)
            }
            Level.NORMAL -> {
                GameSettings(20,20,80,40)
            }
            Level.HARD -> {
                GameSettings(30,30,90,40)
            }
        }
    }

}