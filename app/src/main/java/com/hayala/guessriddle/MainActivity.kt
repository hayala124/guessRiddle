package com.hayala.guessriddle

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.transition.Visibility
import com.hayala.guessriddle.databinding.ActivityMainBinding
import kotlinx.parcelize.Parcelize

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var riddle: String
    private lateinit var state: State
    private lateinit var keyOfMap: String
    private var launcher: ActivityResultLauncher<Intent>? = null
    private val mapOfRiddlesAndAnswers = mutableMapOf(
        "замок" to "Не лает, не кусает, а в дом не пускает?",
        "лёд" to "В огне не горит, в воде не тонет?",
        "часы" to "Что всегда идёт, а с места не сойдёт?",
        "неправильно" to "Какое слово всегда пишется неправильно?",
        "3" to "На столе лежали три огурца и четыре яблока. Ребенок взял со стола одно яблоко. Сколько фруктов осталось на столе?",
        "1" to "Сколько раз из числа 1111 можно вычесть 1?"
        /* "" to "",
 "" to "",
 "" to "",
 "" to "",
 "" to "",
 "" to "",
 "" to "",
 "" to "",
 "" to ""*/
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.btnOpenRiddle.setOnClickListener { openRiddleActivity() }
        binding.btnExit.setOnClickListener { exitApplication() }
        binding.btnAnswer.setOnClickListener { giveAnswerToRiddle() }
        binding.btnStatistics.setOnClickListener { getStatistics() }
        binding.btnStartOver.setOnClickListener { startOver() }

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    val myAnswer = result.data?.getStringExtra("twoName").toString()
                    binding.textYourAnswer.text = myAnswer

                    // Изменения цвета и свойства enabled для кнопок после нажатия на кнопку Проверка (из 2 активити)
                    state.buttonAnswerIsEnabled = !state.buttonAnswerIsEnabled
                    state.buttonAnswerColor = getColor(R.color.gray)

                    if (state.countOfSolvedRiddles.toString().toInt() != 2) {
                        state.buttonOpenRiddleIsEnabled = !state.buttonOpenRiddleIsEnabled
                        state.buttonOpenRiddleColor = getColor(R.color.pink)
                    } else {
                        state.buttonStatisticsIsEnabled = !state.buttonStatisticsIsEnabled
                        state.buttonStatisticsColor = getColor(R.color.pink)
                        binding.textNameYourAnswer.visibility = View.INVISIBLE
                        binding.textYourAnswer.visibility = View.INVISIBLE
                        binding.textRiddle.visibility = View.INVISIBLE
                        state.buttonExitVisibility = !state.buttonExitVisibility
                        //state.buttonExitColor = getColor(R.color.pink)
                        state.buttonStartOverVisibility = !state.buttonStartOverVisibility
                        //state.buttonStartOverColor = getColor(R.color.pink)
                    }

                    // Проверка правильности ответа пользователя
                    val value = mapOfRiddlesAndAnswers[myAnswer]
                    if (value.toString() == riddle) {
                        state.textAnswerColor = Color.GREEN
                        state.countRightAnswer += 1
                    } else {
                        state.textAnswerColor = Color.RED
                        state.countWrongAnswer += 1
                    }
                    // Удаление показанных пользователю загадок
                    mapOfRiddlesAndAnswers.keys.remove(keyOfMap)

                    saveState()
                }
            }

        state = savedInstanceState?.getParcelable(KEY_STATE) ?: State(
                buttonStatisticsIsEnabled = false,
                buttonOpenRiddleIsEnabled = true,
                buttonAnswerIsEnabled = false,
                buttonExitVisibility = false,
                buttonStartOverVisibility = false,
                buttonStatisticsColor = getColor(R.color.gray),
                buttonOpenRiddleColor = getColor(R.color.pink),
                buttonAnswerColor = getColor(R.color.gray),
                buttonExitColor = getColor(R.color.gray),
                buttonStartOverColor = getColor(R.color.gray),
                countOfSolvedRiddles = 0,
                textAnswerColor = getColor(R.color.white),
                countRightAnswer = 0,
                countWrongAnswer = 0
            )
        saveState()
    }

    private fun openRiddleActivity() {
        keyOfMap = mapOfRiddlesAndAnswers.keys.random()
        riddle = mapOfRiddlesAndAnswers[keyOfMap].toString()
        binding.textRiddle.text = riddle
        state.buttonOpenRiddleIsEnabled = !state.buttonOpenRiddleIsEnabled
        state.buttonAnswerIsEnabled = !state.buttonAnswerIsEnabled
        state.buttonOpenRiddleColor = getColor(R.color.gray)
        state.buttonAnswerColor = getColor(R.color.pink)
        state.countOfSolvedRiddles = binding.textCountAnswers.text.toString().toInt() + 1
        state.textAnswerColor = Color.WHITE
        binding.textYourAnswer.text = " "

        saveState()
    }

    private fun giveAnswerToRiddle() {
        val intent = Intent(this, RiddleActivity::class.java)
        launcher?.launch(intent)

        saveState()
    }

    private fun getStatistics() {
        val intent2 = Intent(this, StatisticsActivity::class.java)
        intent2.putExtra("countRightAnswers", state.countRightAnswer.toString())
        intent2.putExtra("countWrongAnswers", state.countWrongAnswer.toString())
        startActivity(intent2)

        saveState()
    }

    private fun startOver() {

    }

    private fun exitApplication() {
        finish()
    }

    private fun saveState() = with(binding) {
        btnStatistics.isEnabled = if (state.buttonStatisticsIsEnabled) true else false
        btnOpenRiddle.isEnabled = if (state.buttonOpenRiddleIsEnabled) true else false
        btnAnswer.isEnabled = if (state.buttonAnswerIsEnabled) true else false
        btnExit.visibility = if (state.buttonExitVisibility) View.VISIBLE else View.GONE
        btnStartOver.visibility = if (state.buttonStartOverVisibility) View.VISIBLE else View.GONE
        btnStatistics.backgroundTintList = (ColorStateList.valueOf(state.buttonStatisticsColor))
        btnOpenRiddle.backgroundTintList = (ColorStateList.valueOf(state.buttonOpenRiddleColor))
        btnAnswer.backgroundTintList = (ColorStateList.valueOf(state.buttonAnswerColor))
        btnExit.backgroundTintList = (ColorStateList.valueOf(state.buttonExitColor))
        btnStartOver.backgroundTintList = (ColorStateList.valueOf(state.buttonStartOverColor))
        textCountAnswers.setText(state.countOfSolvedRiddles.toString())
        textYourAnswer.setBackgroundColor(state.textAnswerColor)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_STATE, state)
    }

    @Parcelize
    class State(
        var buttonStatisticsIsEnabled: Boolean,
        var buttonOpenRiddleIsEnabled: Boolean,
        var buttonAnswerIsEnabled: Boolean,
        var buttonExitVisibility: Boolean,
        var buttonStartOverVisibility: Boolean,
        var buttonStatisticsColor: Int,
        var buttonOpenRiddleColor: Int,
        var buttonAnswerColor: Int,
        var buttonExitColor: Int,
        var buttonStartOverColor: Int,
        var countOfSolvedRiddles: Int,
        var textAnswerColor: Int,
        var countRightAnswer: Int,
        var countWrongAnswer: Int
    ) : Parcelable

    companion object {
        @JvmStatic
        private val KEY_STATE = "STATE"
    }
}