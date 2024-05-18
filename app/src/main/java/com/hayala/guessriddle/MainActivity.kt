package com.hayala.guessriddle

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.hayala.guessriddle.databinding.ActivityMainBinding
import kotlinx.parcelize.Parcelize

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var textViewRiddle: TextView
    private lateinit var riddle: String
    lateinit var state: State
    private var launcher: ActivityResultLauncher<Intent>? = null
    // private var listOfRiddles = listOf("Не лает, не кусает, а в дом не пускает", "В огне не горит, в воде не тонет", "Что всегда идёт, а с места не сойдёт?", "Какое слово всегда пишется неправильно?", "На столе лежали три огурца и четыре яблока. Ребенок взял со стола одно яблоко. Сколько фруктов осталось на столе?")
    //, "", "", "", "", "", "", "", "")
    private val mapOfRiddlesAndAnswers = mapOf(
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

   // private lateinit var count: TextView
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        textViewRiddle = binding.textRiddle

        binding.btnOpenRiddle.setOnClickListener { openRiddleActivity() }
        binding.btnExit.setOnClickListener { exitApplication() }
        binding.btnAnswer.setOnClickListener { giveAnswerToRiddle() }
       // count = binding.textCountAnswers.

       /* launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    myText = result.data?.getStringExtra("twoName").toString()
                    binding.textCountAnswers.text = myText
                }
            }*/

        state = if (savedInstanceState == null) {
            State(
                buttonStatisticsIsEnabled = false,
                buttonOpenRiddleIsEnabled = true,
                buttonAnswerIsEnabled = false,
                buttonExitIsEnabled = false,
                buttonStartOverIsEnabled = false,
                buttonStatisticsColor = getColor(R.color.gray),
                buttonOpenRiddleColor = getColor(R.color.pink),
                buttonAnswerColor = getColor(R.color.gray),
                buttonExitColor = getColor(R.color.gray),
                buttonStartOverColor = getColor(R.color.gray),
                countOfSolvedRiddles = 0
            )
        } else {
            savedInstanceState.getParcelable(KEY_STATE)!!
        }
        saveState()
    }

    private fun openRiddleActivity() {
        riddle = mapOfRiddlesAndAnswers[mapOfRiddlesAndAnswers.keys.random()].toString()
        textViewRiddle.text = riddle
        state.buttonOpenRiddleIsEnabled = !state.buttonOpenRiddleIsEnabled
        state.buttonOpenRiddleColor = getColor(R.color.gray)
        state.buttonAnswerIsEnabled = !state.buttonAnswerIsEnabled
        state.buttonAnswerColor = getColor(R.color.pink)
        count = binding.textCountAnswers.text.toString().toInt() + 1
        state.countOfSolvedRiddles = count
        saveState()
    }

    private fun giveAnswerToRiddle() {
      /*  val intent = Intent(this, RiddleActivity::class.java)
        intent.putExtra("numberPageOne", binding.textCountAnswers.text.toString())
        launcher?.launch(intent)*/

        val intent = Intent(this, RiddleActivity::class.java)
        startActivity(intent)

        state.buttonAnswerIsEnabled = !state.buttonAnswerIsEnabled
        state.buttonAnswerColor = getColor(R.color.gray)

        if (state.countOfSolvedRiddles.toString().toInt() != 10) {
            state.buttonOpenRiddleIsEnabled = !state.buttonOpenRiddleIsEnabled
            state.buttonOpenRiddleColor = getColor(R.color.pink)
        }
        else {
            state.buttonStatisticsIsEnabled = !state.buttonStatisticsIsEnabled
            state.buttonStatisticsColor = getColor(R.color.pink)
        }
        saveState()
    }

    private fun exitApplication() {
        finish()
    }

    private fun saveState() = with(binding) {
        btnStatistics.isEnabled = if (state.buttonStatisticsIsEnabled) true else false
        btnOpenRiddle.isEnabled = if (state.buttonOpenRiddleIsEnabled) true else false
        btnAnswer.isEnabled = if (state.buttonAnswerIsEnabled) true else false
        btnExit.isEnabled = if (state.buttonExitIsEnabled) true else false
        btnStartOver.isEnabled = if (state.buttonStartOverIsEnabled) true else false
        btnStatistics.backgroundTintList = (ColorStateList.valueOf(state.buttonStatisticsColor))
        btnOpenRiddle.backgroundTintList = (ColorStateList.valueOf(state.buttonOpenRiddleColor))
        btnAnswer.backgroundTintList = (ColorStateList.valueOf(state.buttonAnswerColor))
        btnExit.backgroundTintList = (ColorStateList.valueOf(state.buttonExitColor))
        btnStartOver.backgroundTintList = (ColorStateList.valueOf(state.buttonStartOverColor))
        textCountAnswers.setText(state.countOfSolvedRiddles.toString())
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
        var buttonExitIsEnabled: Boolean,
        var buttonStartOverIsEnabled: Boolean,
        var buttonStatisticsColor: Int,
        var buttonOpenRiddleColor: Int,
        var buttonAnswerColor: Int,
        var buttonExitColor: Int,
        var buttonStartOverColor: Int,
        var countOfSolvedRiddles: Int
    ) : Parcelable

    companion object {
        @JvmStatic
        private val KEY_STATE = "STATE"
    }
}