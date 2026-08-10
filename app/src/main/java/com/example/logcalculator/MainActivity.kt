package com.example.logcalculator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var calcbtn: Button
    lateinit var bigDiameter: EditText
    lateinit var smallDiameter: EditText
    lateinit var resultTv: TextView
    lateinit var twoInch: TextView
    lateinit var oneInch: TextView
    lateinit var spillage: TextView
    lateinit var errorText: TextView
    val logCalc: SquareCalculator = SquareCalculator()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        calcbtn = findViewById(R.id.calculateLog)
        bigDiameter = findViewById(R.id.big_input)
        smallDiameter = findViewById(R.id.small_input)
        resultTv = findViewById(R.id.result)
        twoInch = findViewById(R.id.twoInch)
        oneInch = findViewById(R.id.oneInch)
        spillage = findViewById(R.id.spill)
        errorText = findViewById(R.id.textinput_error)
    }

    private fun setupListeners() {
        calcbtn.setOnClickListener {
            try {
                errorText.text = ""
                checkInput()
            }catch (e: IllegalArgumentException){
                errorText.text = e.message
            }
        }
    }

    private fun checkInput() {
        var inputBig = bigDiameter.text.toString()
            .trim()
            .replace(',', '.')
            .toDoubleOrNull()
        var inputSmall = smallDiameter.text.toString()
            .trim()
            .replace(',', '.')
            .toDoubleOrNull()
        if (inputBig == null && inputSmall == null) {
            throw IllegalArgumentException("Faulty or no input detected")
        }
        val big: Double
        val small: Double

        if (inputSmall == null) {
            small = inputBig!!
            big = inputBig
            smallDiameter.setText(big.toString())
        } else if (inputBig == null) {
            big = inputSmall
            small = inputSmall
            bigDiameter.setText(small.toString())
        }else if(inputBig < inputSmall){
            big = inputSmall
            small = inputBig
        }else {
            big = inputBig
            small = inputSmall
        }
        calculate(big,small)
    }

    private fun calculate(bigDia: Double, smallDia: Double) {
        logCalc.doubleDiaBig = bigDia
        logCalc.doubleDiaSmall = smallDia
        var biggestBlock: Double
        var biggestYield: Yield
        biggestBlock = logCalc.getInchBlock(logCalc.ellipsSquare)
        biggestYield = logCalc.getCutAmounts(biggestBlock)
        present(biggestYield, biggestBlock)

    }

    private fun present(biggestYield: Yield, biggestBlock: Double) {
        if (biggestYield.spill > 0) {
            Log.d("DEBUGG", "spill var för hög, ersätter..")
            val biggestBlock = biggestBlock - biggestYield.spill
            val biggestYield = logCalc.getCutAmounts(biggestBlock)
        }
        resultTv.text = "Största block: $biggestBlock tum"
        twoInch.text = "Antal 2 tum: ${biggestYield.twoInches}"
        oneInch.text = "Antal 1 tum: ${biggestYield.oneInches}"
        spillage.text = "Spill: ${biggestYield.spill}"
    }
}