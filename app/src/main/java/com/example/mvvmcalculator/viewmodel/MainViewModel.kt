package com.example.mvvmcalculator.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

class MainViewModel: BaseObservable() {
    var currentInput = ObservableField<String> ()
    var tempInput = ObservableField<String> ()

    var first = 0
    var second = 0
    var result = 0
    var operator = 0
    var stringBuffer = StringBuffer()

    fun inputNumber(number: Int) {
        stringBuffer.append(number)
        currentInput.set(stringBuffer.toString())
        Log.d("inputTest", "number : $number")
    }

    fun doAC() {
        stringBuffer.delete(0, stringBuffer.capacity())
        currentInput.set("")
        operator = 0
    }

    fun doOperation(operation: Int) {
        tempInput.set(stringBuffer.toString())
        stringBuffer.delete(0, stringBuffer.capacity())
        currentInput.set("")
        operator = operation
    }

    fun getTheResult() {
        if (operator != 0) {
            first = tempInput.get()!!.toInt()
            second = currentInput.get()!!.toInt()

            when (operator) {
                PLUS -> currentInput.set(first.plus(second).toString())
                MINUS -> currentInput.set(first.minus(second).toString())
                MULTIPLY -> currentInput.set(first.times(second).toString())
                DIVISION -> currentInput.set(first.div(second).toString())
            }
        }
    }

    companion object {
        const val PLUS = 1
        const val MINUS = 2
        const val MULTIPLY = 3
        const val DIVISION = 4
    }
}