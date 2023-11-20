package com.example.geoquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.geoquiz.databinding.ActivityMainBinding
import com.example.geoquiz.viewmodels.QuizViewModel
import kotlin.math.roundToInt

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var rightAnswersTextView: TextView
    private lateinit var showAnswerButton: Button

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = binding.trueButton
        falseButton = binding.falseButton
        nextButton = binding.nextButton
        prevButton = binding.prevButton
        questionTextView = binding.questionTextView
        rightAnswersTextView = binding.rightAnswersTextView
        showAnswerButton = binding.showAnswerButton

        showAnswerButton.setOnClickListener {
            quizViewModel.isAlreadyViewed = true
            val intent = Intent(this@MainActivity, ViewAnswerActivity::class.java)
            val answer = quizViewModel.currentQuestionAnswer
            intent.putExtra("answer", answer)
            startActivity(intent)
        }

        trueButton.setOnClickListener {
            if (quizViewModel.isAlreadyViewed) {
                Toast.makeText(this, "Ай ай ай, ты теперь не сможешь ответить", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            checkAnswer(true)
            nextQuestionWithUpdate()
        }

        falseButton.setOnClickListener {
            if (quizViewModel.isAlreadyViewed) {
                Toast.makeText(this, "Ай ай ай, ты теперь не сможешь ответить", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            checkAnswer(false)
            nextQuestionWithUpdate()
        }

        nextButton.setOnClickListener {
            nextQuestionWithUpdate()
        }

        prevButton.setOnClickListener {
            prevQuestionWithUpdate()
        }
        updateQuestion()
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText

        if (questionTextResId === R.string.question_silenok) {
            falseButton.visibility = View.INVISIBLE
        } else if (questionTextResId === R.string.question_mmtr) {
            falseButton.visibility = View.INVISIBLE
        } else {
            falseButton.visibility = View.VISIBLE
        }

        questionTextView.setText(questionTextResId)
        rightAnswersTextView.setText(quizViewModel.counterOfRightAnswers.toString())
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val currentQuestionAlreadyAnswered = quizViewModel
            .questionsMap.keys.stream().filter {
                it == quizViewModel.currentQuestionText
            }.count() >= 1

        if (currentQuestionAlreadyAnswered) {
            Toast.makeText(this, R.string.question_already_exists, Toast.LENGTH_SHORT).show()
            return
        }

        val messageResId = if (userAnswer == correctAnswer) {
            quizViewModel.counterOfRightAnswers++
            quizViewModel.questionsMap[quizViewModel.currentQuestionText] = true
            R.string.correct_toast
        } else {
            quizViewModel.questionsMap[quizViewModel.currentQuestionText] = false
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        if (quizViewModel.questionsMap.size == quizViewModel.questionBank.size) {
            val percentRightAnswers =
                (quizViewModel.counterOfRightAnswers * 100 / quizViewModel.questionsMap.size).toDouble()
                    .roundToInt()

            Toast.makeText(
                this,
                "Вы правильно ответили на $percentRightAnswers% вопросов",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

    }

    private fun nextQuestionWithUpdate() {
        quizViewModel.moveToNext()
        updateQuestion()
    }

    private fun prevQuestionWithUpdate() {
        quizViewModel.moveToPrev()
        updateQuestion()
    }
}