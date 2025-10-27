package com.example.calculator

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val calculator = Calculator()
    private var lastResult = ""
    private var isUpdatingProgrammatically = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNumberButtons()
        setupOperatorButtons()
        setupSpecialButtons()
        setupEditText()

        // Initial entrance animation
        animateEntrance()
    }

    private fun setupEditText() {
        // Listen for manual text changes
        binding.txtInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isUpdatingProgrammatically) {
                    // Clear result when user manually edits
                    binding.txtResult.text = ""
                }
            }
        })
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
                appendToInput(button.text.toString())
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
                val currentText = binding.txtInput.text.toString()
                if (currentText.isNotEmpty() && !currentText.endsWith(" ")) {
                    appendToInput(" $operator ")
                }
            }
        }
    }

    private fun setupSpecialButtons() {
        binding.btnBackspace.setOnClickListener {
            animateButton(it)
            val currentText = binding.txtInput.text.toString()
            if (currentText.isNotEmpty()) {
                val newText = if (currentText.endsWith(" ")) {
                    // Remove operator with spaces (e.g., " + ")
                    currentText.dropLast(3)
                } else {
                    // Remove single character
                    currentText.dropLast(1)
                }
                updateInputText(newText)
                binding.txtResult.text = ""
            }
        }

        binding.btnClear.setOnClickListener {
            animateButton(it)
            updateInputText("")
            lastResult = ""
            binding.txtResult.text = ""
            animateClear()
        }

        binding.btnEquals.setOnClickListener {
            animateButton(it)
            val expression = binding.txtInput.text.toString()
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

    private fun appendToInput(text: String) {
        val currentText = binding.txtInput.text.toString()
        val cursorPosition = binding.txtInput.selectionStart

        val newText = StringBuilder(currentText).insert(cursorPosition, text).toString()
        val newCursorPosition = cursorPosition + text.length

        updateInputText(newText)
        binding.txtInput.setSelection(newCursorPosition)
        binding.txtResult.text = ""
    }

    private fun updateInputText(text: String) {
        isUpdatingProgrammatically = true
        binding.txtInput.setText(text)
        binding.txtInput.setSelection(text.length)
        isUpdatingProgrammatically = false
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
            .withEndAction {
                binding.txtInput.alpha = 1f
            }
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
            binding.btn0, binding.btnBackspace, binding.btnClear, binding.btnAdd,
            binding.btnEquals
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