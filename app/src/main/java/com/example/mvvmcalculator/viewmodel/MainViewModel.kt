package com.example.mvvmcalculator.viewmodel

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

class MainViewModel: BaseObservable() {
    var currentInput = ObservableField<String> ()
    var tempInput = ObservableField<String> ()
    var latestInput = ObservableField<String> ()
    var latestResult = ObservableField<String> ()

    private var first = 0.0
    private var second = 0.0
    private var operator = 0
    private var stringBuffer = StringBuffer()

    fun inputNumber(number: Int) {
        if (number == -1) {
            stringBuffer.append(".")
        } else {
            stringBuffer.append(number)
        }
        currentInput.set(stringBuffer.toString())
    }

    fun doAC() {
        stringBuffer.delete(0, stringBuffer.capacity())
        currentInput.set("")
        latestResult.set("")
        operator = 0
    }

    fun doOperation(operation: Int) {

        latestInput.set(stringBuffer.toString())
        tempInput.set(stringBuffer.toString())
        stringBuffer.delete(0, stringBuffer.capacity())
        currentInput.set("")
        operator = operation
    }

    fun getTheResult() {
        Log.d("result", "before temp : ${tempInput.get().toString()} | current : ${currentInput.get().toString()}")

        // 우선적으로 operator와 입력값이 존재하는지 확인
        if (operator != 0 && !currentInput.get().isNullOrBlank()) {

            Log.d("result", "data : ${latestResult.get()}")

            // 가장 최근 result가 저장되어있는지 확인, 존재 유무에 따라 first 및 second value 설정
            if (!latestResult.get().isNullOrBlank()) {
                first = latestResult.get()!!.toDouble()
                second = tempInput.get()!!.toDouble()
            } else {
                first = latestInput.get()!!.toDouble()
                second = currentInput.get()!!.toDouble()
            }

            when (operator) {
                PLUS -> currentInput.set(first.plus(second).toString())
                MINUS -> currentInput.set(first.minus(second).toString())
                MULTIPLY -> currentInput.set(first.times(second).toString())
                DIVISION -> currentInput.set(first.div(second).toString())
            }

            latestResult.set(currentInput.get().toString())
        }

        Log.d("result", "after temp : ${tempInput.get().toString()} | current : ${currentInput.get().toString()} | latest :  ${latestResult.get().toString()}")
    }

    companion object {
        const val PLUS = 1
        const val MINUS = 2
        const val MULTIPLY = 3
        const val DIVISION = 4
    }
}