package com.group.libraryapp.calculator

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JunitCalculatorTest {

    @Test
    fun addTest() {
        //given
        val calculator = Calculator(5)

        //when
        calculator.add(3)

        //then
        //assertThat(확인하고 싶은 값) .isEqualTo(기대값)
        assertThat(calculator.number).isEqualTo(8)
    }

    @Test
    fun minusTest() {
        // given
        val calculator = Calculator(5)

        // when - 테스트 하고 싶은 기능 호출
        calculator.minus(3)

        // then - 검증 결과
       assertThat(calculator.number).isEqualTo(2)
    }

    @Test
    fun divideExceptionTest(){

        // given
        val calculator = Calculator(5)

        // when&then
        val message = assertThrows<IllegalArgumentException>{
            calculator.divide(0)
        }.message

        assertThat(message).isEqualTo("0 으로 나눌수없습니다")
    }
}