package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val calculator = Calculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var expression = ""

        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )

        val operatorButtons = listOf(
            binding.btnAdd, binding.btnSubtract, binding.btnMultiply, binding.btnDivide
        )

        numberButtons.forEach { button ->
            button.setOnClickListener {
                expression += button.text
                binding.txtInput.text = expression
            }
        }

        operatorButtons.forEach { button ->
            button.setOnClickListener {
                expression += " ${button.text} "
                binding.txtInput.text = expression
            }
        }

        binding.btnClear.setOnClickListener {
            expression = ""
            binding.txtInput.text = ""
            binding.txtResult.text = ""
        }

        binding.btnEquals.setOnClickListener {
            val result = calculator.calculate(expression)
            binding.txtResult.text = result.toString()
        }
    }
}
