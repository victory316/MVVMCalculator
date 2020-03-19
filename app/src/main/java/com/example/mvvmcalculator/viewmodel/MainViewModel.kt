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
    private var third = 0.0
    private var operator = 0
    private var stringBuffer = StringBuffer()
    private var newOperation = false
    private var commaActivated = false

    fun inputNumber(number: Int) {

        // [FIX | 200118] : 0 Input 및 콤마 연속적으로 들어올 때의 처리
        if (currentInput.get() != "0") {

            // input이 COMMA일때와 number일때의 처리
            if (number == COMMA) {
                if (!commaActivated && !currentInput.get().isNullOrBlank()) {
                    commaActivated = true
                    stringBuffer.append(".")
                }
            } else {
                stringBuffer.append(number)
            }
        } else {
            if (number == COMMA && !commaActivated && !currentInput.get().isNullOrBlank()) {
                commaActivated = true
                stringBuffer.append(".")
            } else if (number != 0 && number != COMMA) {
                stringBuffer.delete(0, stringBuffer.capacity())
                stringBuffer.append(number)
            }
        }

        currentInput.set(stringBuffer.toString())
    }

    fun doAC() {

        // AC 입력시 연산에 관한 데이터 초기화
        stringBuffer.delete(0, stringBuffer.capacity())
        currentInput.set("")
        tempInput.set("")
        latestResult.set("")
        operator = 0
        commaActivated = false
    }

    fun doOperation(operation: Int) {

        // 입력받은 operation에 따라 연산 이전에 필요한 데이터 설정
        latestInput.set(stringBuffer.toString())
        tempInput.set(stringBuffer.toString())
        stringBuffer.delete(0, stringBuffer.capacity())
        currentInput.set("")
        operator = operation
        newOperation = true
        commaActivated = false
    }

    fun getTheResult() {

        // 우선적으로 operator와 입력값이 존재하는지 확인
        // [FIX | 200128] : 세번째 임시 double 변수를 추가해 연속 연산수행시 값을 저장하도록 함.
        if (operator != 0 && !currentInput.get().isNullOrBlank()) {

            // 가장 최근 result가 저장되어있는지 확인, 존재 유무에 따라 first 및 second value 설정
            if (!latestResult.get().isNullOrBlank()) {

                if (!newOperation) {
                    // case 1 : 신규연산 없이 결과 버튼을 눌렀을 때
                    first = latestResult.get()!!.toDouble()
                    second = third
                } else {
                    // case 2때 : AC 이전 신규연산을 수행했을때 때
                    first = latestResult.get()!!.toDouble()
                    second = currentInput.get()!!.toDouble()
                    third = second
                }

            } else {
                // case 3 : 최근 결과값 없이 AC 수행 혹은 초기에 결과 버튼을 눌렀을 때
                first = latestInput.get()!!.toDouble()
                second = currentInput.get()!!.toDouble()
                third = second
            }

            with (currentInput) {
                when (operator) {
                    PLUS -> set(first.plus(second).toString())
                    MINUS -> set(first.minus(second).toString())
                    MULTIPLY -> set(first.times(second).toString())
                    DIVISION -> set(first.div(second).toString())
                }
            }

            latestResult.set(currentInput.get().toString())
        }

        newOperation = false
    }

    companion object {
        const val PLUS = 1
        const val MINUS = 2
        const val MULTIPLY = 3
        const val DIVISION = 4
        const val COMMA = -1
    }
}