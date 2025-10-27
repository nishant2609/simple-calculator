package com.example.calculator

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val calculator = Calculator()
    private var expression = ""
    private var lastResult = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNumberButtons()
        setupOperatorButtons()
        setupSpecialButtons()

        // Initial entrance animation
        animateEntrance()
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )

        numberButtons.forEach { button ->
            button.setOnClickListener {
                animateButton(it)
                expression += button.text
                updateDisplay()
            }
        }
    }

    private fun setupOperatorButtons() {
        val operatorButtons = mapOf(
            binding.btnAdd to "+",
            binding.btnSubtract to "-",
            binding.btnMultiply to "×",
            binding.btnDivide to "÷"
        )

        operatorButtons.forEach { (button, operator) ->
            button.setOnClickListener {
                animateButton(it)
                if (expression.isNotEmpty() && !expression.endsWith(" ")) {
                    expression += " $operator "
                    updateDisplay()
                }
            }
        }
    }

    private fun setupSpecialButtons() {
        binding.btnClear.setOnClickListener {
            animateButton(it)
            expression = ""
            lastResult = ""
            binding.txtInput.text = ""
            binding.txtResult.text = ""
            animateClear()
        }

        binding.btnEquals.setOnClickListener {
            animateButton(it)
            if (expression.isNotEmpty()) {
                val result = calculator.calculate(expression)
                lastResult = if (result % 1.0 == 0.0) {
                    result.toInt().toString()
                } else {
                    String.format("%.2f", result)
                }
                binding.txtResult.text = lastResult
                animateResult()
            }
        }
    }

    private fun updateDisplay() {
        binding.txtInput.text = expression
        binding.txtInput.alpha = 0f
        binding.txtInput.animate()
            .alpha(1f)
            .setDuration(150)
            .start()
    }

    private fun animateButton(view: View) {
        // Scale animation
        view.animate()
            .scaleX(0.9f)
            .scaleY(0.9f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun animateResult() {
        binding.txtResult.scaleX = 0.8f
        binding.txtResult.scaleY = 0.8f
        binding.txtResult.alpha = 0f

        binding.txtResult.animate()
            .scaleX(1f)
            .scaleY(1f)
            .alpha(1f)
            .setDuration(300)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    private fun animateClear() {
        binding.txtInput.animate()
            .alpha(0f)
            .setDuration(200)
            .start()

        binding.txtResult.animate()
            .alpha(0f)
            .setDuration(200)
            .start()
    }

    private fun animateEntrance() {
        val buttons = listOf(
            binding.btn7, binding.btn8, binding.btn9, binding.btnDivide,
            binding.btn4, binding.btn5, binding.btn6, binding.btnMultiply,
            binding.btn1, binding.btn2, binding.btn3, binding.btnSubtract,
            binding.btn0, binding.btnClear, binding.btnEquals, binding.btnAdd
        )

        buttons.forEachIndexed { index, button ->
            button.alpha = 0f
            button.translationY = 50f
            button.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay((index * 30).toLong())
                .setDuration(300)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }
}