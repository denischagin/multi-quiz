package com.example.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ViewAnswerActivity : AppCompatActivity() {
    private lateinit var answerTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_answer)

        answerTextView = findViewById(R.id.answer_text_view)


        val answer = intent.getBooleanExtra("answer", false)

        val answerText = if (answer) "Да" else "Нет"
        answerTextView.text = answerText
    }
}