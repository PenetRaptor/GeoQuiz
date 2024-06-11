package com.example.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by
    lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

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
            quizViewModel.moveToNext()
            updateQuestion()
            checkAskCompleted()
        }

        updateQuestion()

        cheatButton = findViewById(R.id.cheat_button)
        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle)
    {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        val isLastQuestion = quizViewModel.currentIndex == quizViewModel.questionSize - 1

        // Если это последний вопрос, скрываем кнопку "Next"
        nextButton.visibility = if (isLastQuestion) View.GONE else View.VISIBLE
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (checkAskCompleted()) return

        val correctAnswer = quizViewModel.currentQuestionAnswer
        var messageResId: Int
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.currentCorrectAnswerUp()
        } else {
            messageResId = R.string.incorrect_toast
        }
        if (quizViewModel.isCheater) messageResId = R.string.judgment_toast


        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        ).show()

        quizViewModel.currentQuestionUserResponse(userAnswer) //Запоминание ответа пользователя

        trueButton.isEnabled = false
        falseButton.isEnabled = false
        quizViewModel.currentResponseUp()
        if (checkAskCompleted()) return

        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.currentCorrectAnswerUp()
        } else {
            messageResId = R.string.incorrect_toast
        }
        if (quizViewModel.isCheater) messageResId = R.string.judgment_toast

        Toast.makeText(
            this,
            messageResId,
            Toast.LENGTH_SHORT
        ).show()

        quizViewModel.currentQuestionUserResponse(userAnswer) // Запоминание ответа пользователя

        trueButton.isEnabled = false
        falseButton.isEnabled = false
        quizViewModel.currentResponseUp()

        // Проверяем, является ли текущий вопрос последним в игре
        if (quizViewModel.currentIndex == quizViewModel.questionSize - 1) {
            // Если это последний вопрос, показываем результаты
            checkQuizCompleted()
        }
    }

    private fun checkAskCompleted(): Boolean {
        checkQuizCompleted()
        if (quizViewModel.currentQuestionUserResponse == null) {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            return false
        } else {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            return true
        }
    }

    private fun checkQuizCompleted() {
        val quizSize = quizViewModel.questionSize
        if (quizViewModel.currentResponse == quizSize) {
            val msg =
                "Игра завершена. Правильных ответов ${quizViewModel.currentCorrectAnswer}/${quizSize}"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}

