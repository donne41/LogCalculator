package com.example.logcalculator

import android.os.Bundle
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
        initalValues()
    }


    private fun initalValues() {
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
                logCalc.setInputMetric(false)
            } else {
                inputUnitText.text = "cm"
                inputUnitIsMetric = true
                logCalc.setInputMetric(true)
            }
        }
        outputUnit.setOnClickListener {
            if (outputUnit.isChecked) {
                outputUnitText.text = "tum"
                outputUnitIsMetric = false
                logCalc.setOutputMetric(false)
            } else {
                outputUnitText.text = "cm"
                outputUnitIsMetric = true
                logCalc.setOutputMetric(true)
            }
        }
        thicknessBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var currentValue = thicknessMin + progress
                if (outputUnitIsMetric) {
                    val currentValueText = currentValue * 2.54
                    thicknessText.text = "Tjocklek: ${currentValueText} cm"
                } else {
                    thicknessText.text = "Tjocklek: ${currentValue} tum"
                }
                logCalc.setPreferredThickness(currentValue)
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
        if (inputBig == null && inputSmall == null || inputBig == 0.0 || inputSmall == 0.0) {
            throw IllegalArgumentException("Felaktig eller ingen diameter inslagen")
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
        //logCalc.doubleDiaBig = bigDia
        //logCalc.doubleDiaSmall = smallDia
        //val biggestBlock: Double = logCalc.getInchBlock(logCalc.ellipsSquare)
        //val biggestYield: Yield = logCalc.calculatePlanks(biggestBlock)
        val yield: Yield = logCalc.getYield(bigDia, smallDia)
        if (outputUnitIsMetric) {
            present(logCalc.convertYieldUnitToMetric(yield))
        } else {
            present(yield)
        }
    }

    private fun present(biggestYield: Yield) {
        val selectedUnit: String
        val oneinchFixed: String
        if (outputUnitIsMetric) {
            selectedUnit = "cm"
            oneinchFixed = "2.54 cm"
        } else {
            selectedUnit = "tum"
            oneinchFixed = "1 tum"
        }
        resultTv.text = "Största block: ${biggestYield.blockSize} $selectedUnit"
        twoInch.text = "Antal ${biggestYield.preferredThickness} $selectedUnit: ${biggestYield.preferredPlank}"
        oneInch.text = "Antal $oneinchFixed: ${biggestYield.oneInches}"
        spillage.text = "Spill: ${biggestYield.spill} $selectedUnit"
    }
}
