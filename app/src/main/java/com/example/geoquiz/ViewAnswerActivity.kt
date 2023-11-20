package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.geoquiz.databinding.ActivityViewAnswerBinding

private lateinit var binding: ActivityViewAnswerBinding

class ViewAnswerActivity : AppCompatActivity() {
    private lateinit var answerTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_answer)

        answerTextView = binding.answerTextView

        val answer = intent.getBooleanExtra("answer", false)

        val answerText = if (answer) "Да" else "Нет"
        answerTextView.text = answerText
    }
}