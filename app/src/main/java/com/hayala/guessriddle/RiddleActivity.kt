package com.hayala.guessriddle

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.RadioButton
import com.hayala.guessriddle.databinding.ActivityRiddleBinding
import kotlinx.parcelize.Parcelize

class RiddleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiddleBinding
    private var listOfAnswers = listOf("замок", "лёд", "часы", "неправильно", "3", "1") //, "", "", "", "", "", "", "", "", "")
    lateinit var state: State

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiddleBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.btnCheck.setOnClickListener { buttonCheckPressed() }

        val shuffledList = listOfAnswers.shuffled()

        // Шаг 3: Создание нового списка и копирование перемешанных элементов
        val newList = mutableListOf<String>()
        shuffledList.forEach { newList.add(it) }

        val radioGroup = binding.radioGroupAnswers
        for (option in newList) {
            val radioButton = RadioButton(this)
            radioButton.text = option
            radioGroup.addView(radioButton)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedButton = findViewById<RadioButton>(checkedId)
            state.buttonCheckColor = getColor(R.color.pink)
            state.buttonCheckIsEnabled = true
            binding.btnCheck.isEnabled = state.buttonCheckIsEnabled

           /* var intent2 = Intent(this, MainActivity::class.java)
            intent2.putExtra("numberPageTwo", selectedButton.text.toString())*/
            saveState()
        }

        state = if (savedInstanceState == null) {
            State(
                buttonCheckIsEnabled = false,
                buttonCheckColor = getColor(R.color.gray)
            )
        } else {
            savedInstanceState.getParcelable(KEY_STATE)!!
        }

        saveState()
    }

    /*fun onRadioButtonClicked(view: View) {
        // Получаем ID выбранной радиокнопки
        val radioButton = view as RadioButton
        val button = findViewById<Button>(R.id.button)

        // Активируем кнопку отправки, если была выбрана какая-либо радиокнопка
        if (radioButton.isChecked) {
            button.isEnabled = true
        } else {
            button.isEnabled = false
        }
    }*/

    fun buttonCheckPressed() {
        val data = intent.getStringExtra("numberPageOne")
        val count = data.toString().toInt() + 1
        val intent2 = Intent(this, MainActivity::class.java)
        intent2.putExtra("twoName", count.toString())
        setResult(RESULT_OK, intent2)
        saveState()
        finish()
    }

    private fun saveState() = with(binding) {
        btnCheck.isEnabled = if (state.buttonCheckIsEnabled) true else false
        btnCheck.backgroundTintList = ColorStateList.valueOf(state.buttonCheckColor)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_STATE, state)
    }

    @Parcelize
    class State(
        var buttonCheckIsEnabled: Boolean,
        var buttonCheckColor: Int,
    ) : Parcelable

    companion object {
        @JvmStatic
        private val KEY_STATE = "STATE"
    }
}