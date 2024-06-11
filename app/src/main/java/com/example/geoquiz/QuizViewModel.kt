package com.example.geoquiz

import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel() {

    private var questionBank = listOf(
        Question(R.string.question_australia, true, null, false),
        Question(R.string.question_oceans, true, null, false),
        Question(R.string.question_mideast, false, null, false),
        Question(R.string.question_africa, false, null, false),
        Question(R.string.question_americas, true, null, false),
        Question(R.string.question_asia, true, null, false)
    )

    var currentIndex = 0
    val isCheater get() = questionBank[currentIndex].cheat
    var currentResponse = 0
    var currentCorrectAnswer = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionUserResponse: Boolean?
        get() = questionBank[currentIndex].userResponse

    val questionSize: Int
        get() = questionBank.size

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        if (currentIndex > 0) currentIndex--
        else currentIndex = questionBank.size - 1
    }

    fun currentQuestionUserResponse(userAnswer: Boolean) {
        questionBank[currentIndex].userResponse = userAnswer
    }

    fun currentResponseUp() {
        currentResponse++
    }

    fun currentCorrectAnswerUp() {
        currentCorrectAnswer++
    }

    fun CheckCheat(data: Boolean) {
        questionBank[currentIndex].cheat = data
    }
}
