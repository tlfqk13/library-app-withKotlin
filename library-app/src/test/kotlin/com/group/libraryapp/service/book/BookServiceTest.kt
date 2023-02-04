package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
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
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName(" 책 등록이 정상 동작한다")
    fun saveBookTest() {
        //given
        val request = BookRequest("부의 추월차선", BookType.COMPUTER)

        //when
        bookService.saveBook(request)

        //then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("부의 추월차선")
        assertThat(books[0].type).isEqualTo(BookType.COMPUTER)
    }

    @Test
    @DisplayName(" 책 대출이 정상 동작한다")
    fun loanBookTest() {
        //given
        bookRepository.save(Book.fixture("부의 추월차선"))
        val savedUser = userRepository.save(User("손동규", 30))
        val request = BookLoanRequest("손동규", "부의 추월차선")

        //when
        bookService.loanBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].bookName).isEqualTo("부의 추월차선")
        assertThat(results[0].user.name).isEqualTo(savedUser.name)
        assertThat(results[0].status).isEqualTo(UserLoanStatus.LOANED)

    }

    @Test
    @DisplayName(" 책이 진작 대출되어 있다면, 신규 대출이 실패한다")
    fun loanBookFailTest() {
        //given
        bookRepository.save(Book.fixture("부의 추월차선"))
        val savedUser = userRepository.save(User("손동규", 30))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "부의 추월차선"))
        val request = BookLoanRequest("손동규", "부의 추월차선")

        //when & then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(request)
        }.message
        assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
    }

    @Test
    @DisplayName(" 책 반납이 정상 동작합니다")
    fun returnBookTest() {
        //given
        bookRepository.save(Book.fixture("부의 추월차선"))
        val savedUser = userRepository.save(User("손동규", 30))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "부의 추월차선"))
        val request = BookReturnRequest("손동규", "부의 추월차선")
        //when
        bookService.returnBook(request)

        //then
        val results = userLoanHistoryRepository.findAll()
        assertThat(results[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    @DisplayName(" 책 대여 권수 정상 확인한다")
    fun countLoanedBookTest() {
        //given
        bookRepository.save(Book.fixture("책1"))
        bookRepository.save(Book.fixture("책2"))
        bookRepository.save(Book.fixture("책3"))
        bookRepository.save(Book.fixture("책4"))
        bookRepository.save(Book.fixture("책5"))

        val savedUser = userRepository.save(User("손동규", 30))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "책1"))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "책2"))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "책3", UserLoanStatus.RETURNED))

        //when
        val result = bookService.countLoanedBook()

        //then
        assertThat(result).isEqualTo(2)
    }

    @Test
    @DisplayName(" 분야별 책 권수를 정상 확인한다")
    fun getBookStatisticsTest() {
        //when
        bookRepository.save(Book.fixture("부의 추월차선", BookType.ECONOMY))
        bookRepository.save(Book.fixture("클린코드", BookType.COMPUTER))
        bookRepository.save(Book.fixture("클린아키택쳐", BookType.COMPUTER))
        bookRepository.save(Book.fixture("일본어", BookType.LANGUAGE))
        bookRepository.save(Book.fixture("독일어", BookType.LANGUAGE))
        bookRepository.save(Book.fixture("중국어", BookType.LANGUAGE))
        //given
        val results = bookService.getBookStatistics()

        //then
        assertThat(results).hasSize(3)
        assertCount(results, BookType.COMPUTER, 2L)
        assertCount(results, BookType.LANGUAGE, 3L)
        assertCount(results, BookType.ECONOMY, 1L)

    }

    private fun assertCount(results: List<BookStatResponse>, type: BookType, count: Long) {
        assertThat(results.first { result -> result.type == type }.count).isEqualTo(count)
    }


}





















