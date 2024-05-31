package com.hayala.guessriddle

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.widget.RadioButton
import com.hayala.guessriddle.databinding.ActivityRiddleBinding
import kotlinx.parcelize.Parcelize

class RiddleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiddleBinding
    private lateinit var selectedButton: RadioButton
    private var listOfAnswers = listOf("замок", "лёд", "часы", "неправильно", "3", "1", "эхо", "якорь", "столовые приборы", "возможность", "проблемы", "возраст", "уроки", "вишня", "дорога", "лестница", "безрукий человек", "корм для животных", "бумеранг")

    private lateinit var state: State

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiddleBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.btnCheck.setOnClickListener { buttonCheckPressed() }

        val radioGroup = binding.radioGroupAnswers

        for (option in listOfAnswers.shuffled()) {
            val radioButton = RadioButton(this)
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            radioButton.text = option
            radioGroup.addView(radioButton)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            selectedButton = findViewById(checkedId)
            state.buttonCheckColor = getColor(R.color.pink)
            state.buttonCheckIsEnabled = true
            binding.btnCheck.isEnabled = state.buttonCheckIsEnabled

            saveState()
        }

        state = savedInstanceState?.getParcelable(KEY_STATE) ?: State(
            buttonCheckIsEnabled = false,
            buttonCheckColor = getColor(R.color.gray),
        )

        saveState()
    }


    private fun buttonCheckPressed() {
        val intent2 = Intent(this, MainActivity::class.java)
        intent2.putExtra("twoName", selectedButton.text.toString())
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
        var buttonCheckColor: Int
        //var intent: Unit
    ) : Parcelable

    companion object {
        @JvmStatic private val KEY_STATE = "STATE"
    }
}