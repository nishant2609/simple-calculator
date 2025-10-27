package com.example.calculator

class Calculator {
    fun calculate(expression: String): Double {
        return try {
            val tokens = expression.split(" ")
            if (tokens.size < 3) return 0.0

            var result = tokens[0].toDouble()

            var i = 1
            while (i < tokens.size) {
                val operator = tokens[i]
                val next = tokens[i + 1].toDouble()

                result = when (operator) {
                    "+" -> result + next
                    "-" -> result - next
                    "×", "*" -> result * next
                    "÷", "/" -> result / next
                    else -> result
                }
                i += 2
            }
            result
        } catch (e: Exception) {
            0.0
        }
    }
}
