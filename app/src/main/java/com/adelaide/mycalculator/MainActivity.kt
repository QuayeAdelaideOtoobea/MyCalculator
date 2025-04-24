package com.adelaide.mycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adelaide.mycalculator.ui.theme.MyCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var expression by remember { mutableStateOf("") }

    fun onButtonClick(value: String) {
        expression = when (value) {
            "=" -> {
                try {
                    evaluateExpression(expression)
                } catch (e: Exception) {
                    "Error"
                }
            }
            "C" -> ""
            else -> expression + value
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = expression,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color.Black
        )

        val buttons = listOf(
            listOf("7", "8", "9", "÷"),
            listOf("4", "5", "6", "×"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "=", "+"),
            listOf("C")
        )

        for (row in buttons) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (button in row) {
                    CalculatorButton(text = button, onClick = { onButtonClick(button) })
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .size(80.dp)
            .background(Color.LightGray)
            .clickable { onClick() }
    ) {
        Text(text = text, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

fun evaluateExpression(expression: String): String {
    val expr = expression.replace("×", "*").replace("÷", "/")

    return try {
        val tokens = Regex("([-+*/])").split(expr).map { it.trim() }
        val operators = Regex("([-+*/])").findAll(expr).map { it.value }.toList()

        if (tokens.isEmpty()) return "0"

        var result = tokens[0].toDoubleOrNull() ?: return "Error"

        for (i in operators.indices) {
            val next = tokens.getOrNull(i + 1)?.toDoubleOrNull() ?: return "Error"
            result = when (operators[i]) {
                "+" -> result + next
                "-" -> result - next
                "*" -> result * next
                "/" -> result / next
                else -> return "Error"
            }
        }
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}
