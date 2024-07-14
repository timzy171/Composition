package com.example.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.composition.R
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, minCountOfAnswers : Int) {
    textView.text = textView.context.getString(
        R.string.required_score,
        minCountOfAnswers.toString())
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, scoreAnswers : Int) {
    textView.text = textView.context.getString(R.string.score_answers, scoreAnswers.toString())
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, requiredPercentage : Int) {
    textView.text = textView.context.getString(R.string.required_percentage, requiredPercentage.toString())
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult) {
    val scorePercentage = (gameResult.countOfRightAnswers.toDouble() / gameResult.countOfQuestions.toDouble() * 100).toInt()
    textView.text = textView.context.getString(R.string.score_percentage, scorePercentage.toString())
}

@BindingAdapter("emojiResult")
fun bindEmojiResult(imageView: ImageView, winner : Boolean) {
    if (winner) {
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_smile))
    } else {
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.ic_loose))
    }
}