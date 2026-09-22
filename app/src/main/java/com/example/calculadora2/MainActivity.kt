package com.example.calculadora2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var tvExpression: TextView

    private var num1 = ""
    private var num2 = ""
    private var operator = ""
    private var currentNumber = ""
    private var result = ""
    private var justEvaluated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)
        tvExpression = findViewById(R.id.tvExpression)

        bindNumberButtons()
        bindOperatorButtons()
        bindFunctionButtons()
    }

    private fun bindNumberButtons() {
        val ids = intArrayOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        ids.forEachIndexed { index, id ->
            findViewById<Button>(id).setOnClickListener {
                onDigitPressed(index.toString())
            }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener { onDotPressed() }
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { onSignToggle() }
    }

    private fun bindOperatorButtons() {
        findViewById<Button>(R.id.btnAdd).setOnClickListener { onOperatorPressed("+") }
        findViewById<Button>(R.id.btnSub).setOnClickListener { onOperatorPressed("-") }
        findViewById<Button>(R.id.btnMul).setOnClickListener { onOperatorPressed("×") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { onOperatorPressed("÷") }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { onPercentPressed() }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { onEqualsPressed() }
    }

    private fun bindFunctionButtons() {
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnDelete).setOnClickListener { onDeletePressed() }
    }

    private fun onDigitPressed(digit: String) {
        if (justEvaluated) {
            if (operator.isEmpty()) {
                num1 = ""
            }
            result = ""
            justEvaluated = false
            currentNumber = ""
        }
        currentNumber += digit
        tvResult.text = currentNumber
        updateExpression()
    }

    private fun onDotPressed() {
        if (justEvaluated) {
            currentNumber = "0"
            justEvaluated = false
            tvResult.text = currentNumber
        }
        if (!currentNumber.contains(".")) {
            currentNumber = if (currentNumber.isEmpty()) "0." else currentNumber + "."
            tvResult.text = currentNumber
        }
    }

    private fun onOperatorPressed(op: String) {
        if (num1.isNotEmpty() && operator.isNotEmpty() && currentNumber.isNotEmpty()) {
            num1 = calculate(num1, currentNumber, operator)
            tvResult.text = num1
        } else if (num1.isEmpty() && currentNumber.isNotEmpty()) {
            num1 = currentNumber
        }
        operator = op
        currentNumber = ""
        result = ""
        justEvaluated = false
        updateExpression()
    }

    private fun onEqualsPressed() {
        if (num1.isNotEmpty() && operator.isNotEmpty() && currentNumber.isNotEmpty()) {
            result = calculate(num1, currentNumber, operator)
            tvResult.text = result
            num1 = result
            num2 = currentNumber
            currentNumber = ""
            operator = ""
            justEvaluated = true
            tvExpression.text = "$num2 ="
        }
    }

    private fun onSignToggle() {
        if (currentNumber.isEmpty() && result.isEmpty() && num1.isEmpty()) return
        val value = if (currentNumber.isNotEmpty()) currentNumber else if (result.isNotEmpty()) result else num1
        val toggled = if (value.startsWith("-")) value.drop(1) else "-$value"
        if (currentNumber.isNotEmpty()) {
            currentNumber = toggled
        } else {
            num1 = toggled
            result = toggled
        }
        tvResult.text = toggled
    }

    private fun onPercentPressed() {
        val value = if (currentNumber.isNotEmpty()) currentNumber else result
        if (value.isEmpty() || value == "0") return
        val pct = simpleFormat(parse(value) / 100.0)
        if (currentNumber.isNotEmpty()) currentNumber = pct
        result = pct
        tvResult.text = pct
    }

    private fun onDeletePressed() {
        if (justEvaluated) {
            clearAll()
            return
        }
        if (currentNumber.isNotEmpty()) {
            currentNumber = currentNumber.dropLast(1)
            tvResult.text = if (currentNumber.isEmpty()) "0" else currentNumber
        } else if (operator.isNotEmpty()) {
            operator = ""
            updateExpression()
        } else if (num1.isNotEmpty()) {
            num1 = num1.dropLast(1)
            tvResult.text = if (num1.isEmpty()) "0" else num1
        }
    }

    @SuppressLint("SetTextI18n")
    private fun clearAll() {
        num1 = ""
        num2 = ""
        operator = ""
        currentNumber = ""
        result = ""
        justEvaluated = false
        tvResult.text = "0"
        tvExpression.text = ""
    }

    private fun updateExpression() {
        tvExpression.text = buildString {
            append(num1)
            if (operator.isNotEmpty()) append(" $operator ")
            if (justEvaluated && num2.isNotEmpty()) append(num2).append(" =")
        }
    }

    private fun calculate(a: String, b: String, op: String): String {
        val x = parse(a)
        val y = parse(b)
        return try {
            when (op) {
                "+" -> simpleFormat(x + y)
                "-" -> simpleFormat(x - y)
                "×" -> simpleFormat(x * y)
                "÷" -> {
                    if (y == 0.0) "Error"
                    else simpleFormat(x / y)
                }
                else -> b
            }
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun parse(s: String): Double =
        if (s.endsWith("%")) s.dropLast(1).toDouble() / 100.0 else s.toDouble()

    private fun simpleFormat(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        val symbols = DecimalFormatSymbols(Locale.US)
        val df = DecimalFormat("#.##########", symbols)
        return df.format(value)
    }
}