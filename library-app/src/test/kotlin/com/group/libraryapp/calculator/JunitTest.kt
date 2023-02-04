package com.group.libraryapp.calculator

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JunitTest {

    // beforeAll -> beforeEach -> test -> afterEach -> afterAll

    companion object{

        @BeforeAll
        @JvmStatic
        fun beforeAll(){
            println("모든 테스트 시작전")
        }

        @AfterAll
        @JvmStatic
        fun afterAll(){
            println("모든 테스트 종료후")
        }
    }


    @BeforeEach // 각 테스트 메소드가 실행되기전 1번 실행
    fun beforeEach() {
        println("각 테스트 시작 전")
    }
    
    @AfterEach
    fun afterEach(){
        println("각 테스트 종료 후")
    }

    @Test
    fun test1() {
        println("테스트 1")
    }

    @Test
    fun test2() {
        println("테스트 2")
    }
}