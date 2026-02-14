package com.example.simplecalculator

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.simplecalculator.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.text.DecimalFormat
import android.content.Intent
import android.net.Uri

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentNumber = ""
    private var previousNumber = ""
    private var operation = ""
    private var isNewOperation = true

    private val decimalFormat = DecimalFormat("#.##########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupNavigationDrawer()
        setupNumberButtons()
        setupOperationButtons()
        setupFunctionButtons()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)

        // Handle hamburger menu click
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupNavigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_checkCALC -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ososzz/CalcYouLater"))
                    startActivity(intent)
                }
                R.id.nav_github -> {
                    // Open GitHub page
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ososzz"))
                    startActivity(intent)
                }

            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setupNumberButtons() {
        val numberButtons = listOf(
            binding.btn0 to "0",
            binding.btn1 to "1",
            binding.btn2 to "2",
            binding.btn3 to "3",
            binding.btn4 to "4",
            binding.btn5 to "5",
            binding.btn6 to "6",
            binding.btn7 to "7",
            binding.btn8 to "8",
            binding.btn9 to "9",
            binding.btnDecimal to "."
        )

        numberButtons.forEach { (button, value) ->
            button.setOnClickListener {
                if (isNewOperation) {
                    currentNumber = ""
                    isNewOperation = false
                }

                if (value == "." && currentNumber.contains(".")) {
                    return@setOnClickListener
                }

                currentNumber += value
                updateDisplay(currentNumber)
            }
        }
    }

    private fun setupOperationButtons() {
        binding.btnAdd.setOnClickListener { setOperation("+") }
        binding.btnSubtract.setOnClickListener { setOperation("-") }
        binding.btnMultiply.setOnClickListener { setOperation("×") }
        binding.btnDivide.setOnClickListener { setOperation("÷") }

        binding.btnEquals.setOnClickListener {
            calculate()
        }
    }

    private fun setupFunctionButtons() {
        binding.btnClear.setOnClickListener {
            currentNumber = ""
            previousNumber = ""
            operation = ""
            isNewOperation = true
            updateDisplay("0")
        }
    }

    private fun setOperation(op: String) {
        if (currentNumber.isEmpty() && previousNumber.isEmpty()) {
            return
        }

        if (currentNumber.isNotEmpty() && previousNumber.isNotEmpty()) {
            calculate()
        }

        operation = op
        previousNumber = currentNumber
        currentNumber = ""
        isNewOperation = false
    }

    private fun calculate() {
        if (previousNumber.isEmpty() || currentNumber.isEmpty() || operation.isEmpty()) {
            return
        }

        val num1 = previousNumber.toDoubleOrNull() ?: return
        val num2 = currentNumber.toDoubleOrNull() ?: return

        val result = when (operation) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> {
                if (num2 == 0.0) {
                    updateDisplay("Error")
                    resetCalculator()
                    return
                }
                num1 / num2
            }
            else -> return
        }

        currentNumber = formatResult(result)
        previousNumber = ""
        operation = ""
        isNewOperation = true
        updateDisplay(currentNumber)
    }

    private fun formatResult(result: Double): String {
        return if (result % 1 == 0.0) {
            result.toLong().toString()
        } else {
            decimalFormat.format(result)
        }
    }

    private fun resetCalculator() {
        currentNumber = ""
        previousNumber = ""
        operation = ""
        isNewOperation = true
    }

    private fun updateDisplay(value: String) {
        binding.tvDisplay.text = if (value.isEmpty()) "0" else value
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}