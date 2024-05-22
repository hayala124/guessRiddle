package com.hayala.guessriddle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hayala.guessriddle.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.buttonGetTheMainPage.setOnClickListener { getTheMainPage() }
        val countRightAnswer = intent.getStringExtra("countRightAnswers")
        val countWrongAnswer = intent.getStringExtra("countWrongAnswers")
        binding.textCountRightAnswer.text = countRightAnswer.toString()
        binding.textCountWrongAnswer.text = countWrongAnswer.toString()

    }

    private fun getTheMainPage() {
        finish()
    }
}