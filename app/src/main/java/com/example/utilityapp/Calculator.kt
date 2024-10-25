package com.example.utilityapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class Calculator : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private var currentExpression = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        resultTextView = findViewById(R.id.resultTextView)

        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        val buttonIds = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide,
            R.id.buttonDecimal, R.id.buttonLeftParen, R.id.buttonRightParen
        )

        for (id in buttonIds) {
            findViewById<Button>(id).setOnClickListener { onButtonClick(it as Button) }
        }

        findViewById<Button>(R.id.buttonClear).setOnClickListener { clearExpression() }
        findViewById<Button>(R.id.buttonDelete).setOnClickListener { deleteLastCharacter() }
        findViewById<Button>(R.id.buttonEquals).setOnClickListener { evaluateExpression() }
    }

    private fun onButtonClick(button: Button) {
        currentExpression.append(button.text)
        updateDisplay()
    }

    private fun clearExpression() {
        currentExpression.clear()
        updateDisplay()
    }

    private fun deleteLastCharacter() {
        if (currentExpression.isNotEmpty()) {
            currentExpression.deleteCharAt(currentExpression.length - 1)
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        resultTextView.text = currentExpression.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun evaluateExpression() {
        try {
            val result = evaluateInfixExpression(currentExpression.toString())
            resultTextView.text = result.toString()
            currentExpression.clear()
            currentExpression.append(result)
        } catch (e: Exception) {
            resultTextView.text = "Error"
        }
    }

    private fun evaluateInfixExpression(expression: String): Double {
        val tokens = tokenizeExpression(expression)
        val postfixExpression = infixToPostfix(tokens)
        return evaluatePostfixExpression(postfixExpression)
    }

    private fun tokenizeExpression(expression: String): List<String> {
        return expression.replace("(", " ( ")
            .replace(")", " ) ")
            .replace("+", " + ")
            .replace("-", " - ")
            .replace("*", " * ")
            .replace("/", " / ")
            .trim()
            .split("\\s+".toRegex())
    }

    private fun infixToPostfix(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val operatorStack = Stack<String>()

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> output.add(token)
                token == "(" -> operatorStack.push(token)
                token == ")" -> {
                    while (operatorStack.isNotEmpty() && operatorStack.peek() != "(") {
                        output.add(operatorStack.pop())
                    }
                    if (operatorStack.isNotEmpty() && operatorStack.peek() == "(") {
                        operatorStack.pop()
                    }
                }
                token in setOf("+", "-", "*", "/") -> {
                    while (operatorStack.isNotEmpty() && precedence(operatorStack.peek()) >= precedence(token)) {
                        output.add(operatorStack.pop())
                    }
                    operatorStack.push(token)
                }
            }
        }

        while (operatorStack.isNotEmpty()) {
            output.add(operatorStack.pop())
        }

        return output
    }

    private fun precedence(operator: String): Int = when (operator) {
        "+", "-" -> 1
        "*", "/" -> 2
        else -> 0
    }

    private fun evaluatePostfixExpression(postfixExpression: List<String>): Double {
        val stack = Stack<Double>()

        for (token in postfixExpression) {
            when {
                token.toDoubleOrNull() != null -> stack.push(token.toDouble())
                token in setOf("+", "-", "*", "/") -> {
                    val b = stack.pop()
                    val a = stack.pop()
                    stack.push(when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> a / b
                        else -> throw IllegalArgumentException("Unknown operator: $token")
                    })
                }
            }
        }

        return stack.pop()
    }
}