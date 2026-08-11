package com.example.logcalculator

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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
    lateinit var inputUnit: SwitchCompat
    lateinit var outputUnit: SwitchCompat
    lateinit var inputUnitText: TextView
    lateinit var outputUnitText: TextView
    lateinit var thicknessBar: SeekBar
    lateinit var thicknessText: TextView

    var inputUnitIsMetric: Boolean = true
    var outputUnitIsMetric: Boolean = false
    private val thicknessMin: Int = 1
    private val thicknessMax: Int = 5
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
        val initalThickness = 1
        val initalProgress = initalThickness - thicknessMin
        val range = thicknessMax - thicknessMin
        thicknessBar.progress = initalProgress
        thicknessBar.max = range
        outputUnit.isChecked = true
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
        inputUnit = findViewById(R.id.input_switch)
        outputUnit = findViewById(R.id.output_switch)
        inputUnitText = findViewById(R.id.inputUnit)
        outputUnitText = findViewById(R.id.outputUnit)
        thicknessBar = findViewById(R.id.thickness_bar)
        thicknessText = findViewById(R.id.selected_thickness)
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
        inputUnit.setOnClickListener {
            if (inputUnit.isChecked) {
                inputUnitText.text = "tum"
                inputUnitIsMetric = false
            } else {
                inputUnitText.text = "cm"
                inputUnitIsMetric = true
            }
        }
        outputUnit.setOnClickListener {
            if (outputUnit.isChecked) {
                outputUnitText.text = "tum"
                outputUnitIsMetric = false
            } else {
                outputUnitText.text = "cm"
                outputUnitIsMetric = true
            }
        }
        thicknessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val currentValue = thicknessMin + progress
                thicknessText.text = "Tjocklek: $currentValue"
                // send this value to priority yield.
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
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
        val biggestBlock: Double = logCalc.getInchBlock(logCalc.ellipsSquare)
        val biggestYield: Yield = logCalc.getCutAmounts(biggestBlock)
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
