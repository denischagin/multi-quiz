package com.example.geoquiz.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.geoquiz.R
import com.example.geoquiz.models.Question

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    var currentIndex = 0
    var counterOfRightAnswers = 0
    var questionsMap = HashMap<Int, Boolean>()
    var questionBank = listOf(
        Question(R.string.question_negr, true),
        Question(R.string.question_silenok, true),
        Question(R.string.question_adaptive, false),
        Question(R.string.question_android, true),
        Question(R.string.question_spring, false),
        Question(R.string.question_sheshin, false),
        Question(R.string.question_mmtr, false),
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = (currentIndex + questionBank.size - 1) % questionBank.size
    }


    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}