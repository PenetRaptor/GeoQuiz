package com.example.geoquiz

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private  lateinit var trueButton: Button
    private  lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView


    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans,
            true),
        Question(R.string.question_mideast,
            false),
        Question(R.string.question_africa,
            false),
        Question(R.string.question_americas
            , true),
        Question(R.string.question_asia,
            true))
    private var currentIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        trueButton = findViewById(R.id.button_true)
        trueButton.setOnClickListener{view: View ->
            checkAnswer(true)
        }

        falseButton = findViewById(R.id.button_false)
        falseButton.setOnClickListener{view: View ->
            checkAnswer(false)
        }

        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.textViewQuestion)

        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            val questionTextResId = questionBank[currentIndex].textResId
            questionTextView.setText(questionTextResId)
        }
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer:Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId,
            Toast.LENGTH_SHORT)
            .show()
    }

}

