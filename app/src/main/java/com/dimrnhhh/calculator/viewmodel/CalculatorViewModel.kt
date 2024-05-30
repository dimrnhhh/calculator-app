package com.dimrnhhh.calculator.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dimrnhhh.calculator.state.CalculatorState
import com.dimrnhhh.calculator.utils.CalculatorAction
import com.dimrnhhh.calculator.utils.CalculatorOperation

class CalculatorViewModel: ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set
    fun onAction(action: CalculatorAction) {
        when(action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.Operation -> enterOperation(action.operation)
            is CalculatorAction.Calculate -> performCalculate()
            is CalculatorAction.Delete -> performDeletion()
        }
    }

    companion object {
        private const val MAX_NUMBER_LENGTH = 8
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if(state.firstNumber.length >= MAX_NUMBER_LENGTH) {
                return
            }
            state = state.copy(
                firstNumber = state.firstNumber + number
            )
            return
        }
        if (state.secondNumber.length >= MAX_NUMBER_LENGTH) {
            return
        }
        state = state.copy(
            secondNumber = state.secondNumber + number
        )
    }

    private fun enterDecimal() {
        if (state.operation == null && !state.firstNumber.contains(".") && state.firstNumber.isNotBlank()) {
            state = state.copy(
                firstNumber = state.firstNumber + "."
            )
            return
        }
        if (!state.secondNumber.contains(".") && state.secondNumber.isNotBlank()) {
            state = state.copy(
                secondNumber = state.secondNumber + "."
            )
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if(state.firstNumber.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun performCalculate() {
        val firstNumber = state.firstNumber.toDoubleOrNull()
        val secondNumber = state.secondNumber.toDoubleOrNull()
        if (firstNumber != null && secondNumber != null) {
            val result = when(state.operation) {
                is CalculatorOperation.Add -> firstNumber + secondNumber
                is CalculatorOperation.Substract -> firstNumber - secondNumber
                is CalculatorOperation.Multiply -> firstNumber * secondNumber
                is CalculatorOperation.Divide -> firstNumber / secondNumber
                null -> return
            }
            state = state.copy(
                firstNumber = result.toString().take(10),
                secondNumber = "",
                operation = null
            )
        }
    }

    private fun performDeletion() {
        when {
            state.secondNumber.isNotBlank() -> state = state.copy(
                secondNumber = state.secondNumber.dropLast(1)
            )
            state.operation != null -> state = state.copy(
                operation = null
            )
            state.firstNumber.isNotBlank() -> state = state.copy(
                firstNumber = state.firstNumber.dropLast(1)
            )
        }
    }
}