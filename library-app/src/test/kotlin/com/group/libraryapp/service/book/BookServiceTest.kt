package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun clean(){
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }
    
    @Test
    @DisplayName(" 책 등록이 정상 동작한다")
    fun saveBookTest(){
        //given
        val request = BookRequest("부의 추월차선")

        //when
        bookService.saveBook(request)

        //then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("부의 추월차선")
    }

    @Test
    @DisplayName(" 책 대출이 정상 동작한다")
    fun loanBookTest(){
        //given
        bookRepository.save(Book("부의 추월차선"))
        val savedUser = userRepository.save(User("손동규",30))
        val request = BookLoanRequest("손동규","부의 추월차선")
        
        //when
        bookService.loanBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("부의 추월차선")
        assertThat(results[0].user.name).isEqualTo(savedUser.name)
        assertThat(results[0].isReturn).isFalse

    }

    @Test
    @DisplayName(" 책이 진작 대출되어 있다면, 신규 대출이 실패한다")
    fun loanBookFailTest(){
        //given
        bookRepository.save(Book("부의 추월차선"))
        val savedUser = userRepository.save(User("손동규",30))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser,"부의 추월차선",false))
        val request = BookLoanRequest("손동규","부의 추월차선")

        //when & then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }
    
    @Test
    @DisplayName(" 책 반납이 정상 동작합니다")
    fun returnBookTest(){
        //given
        bookRepository.save(Book("부의 추월차선"))
        val savedUser = userRepository.save(User("손동규",30))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser,"부의 추월차선",false))
        val request = BookReturnRequest("손동규","부의 추월차선")
        //when
        bookService.returnBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results[0].isReturn).isTrue
    }

    
}