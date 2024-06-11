package com.example.geoquiz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, var userResponse: Boolean?, var cheat: Boolean)

