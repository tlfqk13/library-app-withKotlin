package com.group.libraryapp.calculator

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

fun main() {
    val calculatorTest = CalculatorTest()
    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multiplyTest()
    calculatorTest.divideTest()
    calculatorTest.divideExceptionTest()
}

class CalculatorTest {

    @Test
    fun addTest() {
        // given
        val calculator = Calculator(5)

        // when - 테스트 하고 싶은 기능 호출
        calculator.add(3)

        // data class 인 경우
       /* val expectedCalculator = Calculator(8)
        if(calculator != expectedCalculator){
            throw IllegalStateException()
        }*/

        // then - 검증 결과
        if(calculator.number != 8){
            throw IllegalStateException()
        }
    }

    @Test
    fun minusTest() {
        // given
        val calculator = Calculator(5)

        // when - 테스트 하고 싶은 기능 호출
        calculator.minus(3)

        // then - 검증 결과
        if(calculator.number != 2){
            throw IllegalStateException()
        }
    }

    @Test
    fun multiplyTest() {
        // given
        val calculator = Calculator(5)

        // when - 테스트 하고 싶은 기능 호출
        calculator.multiply(3)

        // then - 검증 결과
        if(calculator.number != 15){
            throw IllegalStateException()
        }
    }

    @Test
    fun divideTest() {
        // given
        val calculator = Calculator(5)

        // when - 테스트 하고 싶은 기능 호출
        calculator.divide(2)

        // then - 검증 결과
        if(calculator.number != 2){
            throw IllegalStateException()
        }
    }

    @Test
    fun divideExceptionTest() {
        // given
        val calculator = Calculator(5)

        // when - 테스트 하고 싶은 기능 호출
        try {
            calculator.divide(0)
        }catch (e:IllegalArgumentException){
            // 테스트 성공!!
            return
        }catch (e:Exception){
            //테스트 실패
            throw IllegalStateException()
        }
        throw IllegalStateException("기대하는 예외가 발생하지 않았습니다")
    }
}