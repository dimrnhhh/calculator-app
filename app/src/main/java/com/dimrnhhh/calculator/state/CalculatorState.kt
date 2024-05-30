package com.dimrnhhh.calculator.state

import com.dimrnhhh.calculator.utils.CalculatorOperation

data class CalculatorState(
    val firstNumber: String = "",
    val secondNumber: String = "",
    val operation: CalculatorOperation? = null,
)
