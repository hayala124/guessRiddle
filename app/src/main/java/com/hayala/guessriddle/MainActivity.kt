package com.hayala.guessriddle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hayala.guessriddle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenRiddle.setOnClickListener {openRiddleActivity()}
    }

    private fun openRiddleActivity() {
        val intent = Intent(this, RiddleActivity::class.java)
        startActivity(intent)
    }
}