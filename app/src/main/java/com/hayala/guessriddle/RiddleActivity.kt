package com.hayala.guessriddle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hayala.guessriddle.databinding.ActivityMainBinding
import com.hayala.guessriddle.databinding.ActivityRiddleBinding

class RiddleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiddleBinding
    private var listOfRiddles = listOf("Не лает, не кусает, а в дом не пускает", "В огне не горит, в воде не тонет", "Что всегда идёт, а с места не сойдёет?", "", "", "", "", "", "", "", "", "", "")
    private var listOfAnswers = listOf("Замок", "лёд", "часы", "", "", "", "", "", "", "", "", "", "", "", "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiddleBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}