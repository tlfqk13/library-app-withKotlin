package com.group.libraryapp.domain.user.loanhistory

import com.group.libraryapp.domain.book.Book
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class User constructor( //constructor를 명시하면 실제 User entity가 생성되는 로직을 찾을 수 있다.

    var name: String, // setter는 public이기 때문에 updateName 안쓰고 setter를 사용할 '수도' 있다

    val age: Int?,

    // 모든 프로퍼티를 생성자에 넣거나
    // 프로퍼티를 생성자 혹은 클래스 body 안에 구분해서 넣을 때 명확한 기준
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    // 변수는 불변이지만 컬렉션은 가변
    val userLoanHistories: MutableList<UserLoanHistory> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun loanBook(book: Book) {
        this.userLoanHistories.add(UserLoanHistory(this, book.name))
    }

    fun returnBook(bookName: String) {
        this.userLoanHistories.first { hisory -> hisory.bookName == bookName }.doReturn()
    }
}