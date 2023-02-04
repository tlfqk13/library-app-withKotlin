package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.User
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun saveUserTest() {
        // given
        val request = UserCreateRequest("손동규", 30)

        //when
        userService.saveUser(request)

        //then
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("손동규")
        assertThat(results[0].age).isEqualTo(30)
    }

    @Test
    fun getUsersTest() {
        //given
        userRepository.saveAll(
            listOf(
                User("A", 30),
                User("B", null),
            )
        )

        //when
        val results = userService.getUsers()

        //then
        assertThat(results).hasSize(2) // [UserResponse(), UserResponse()]
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A", "B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(30, null)
    }
    // 왜 생성 테스트와 조회 테스트를 같이 돌리면 실패하는가
    // 2개가 나오길 기대했는데 3개가 나옴 
    // 두 테스트는 spring Context - H2 를 공유
    // 생성 테스트에서 1명을 생성 -> 조회 테스트에서 2명을 생성 = 3명
    // 테스트가 끝나면 공유 자원인 DB를 깨끗하게 해주자 - afterEach를 활용하자

    @Test
    fun updateUserNameTest() {
        //given
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id!!, "B")

        //when
        userService.updateUserName(request)

        //then
        val result = userRepository.findAll()[0]
        assertThat(request.name).isEqualTo("B")
    }

    @Test
    @DisplayName("유저 삭제 기능이 정상 동작한다")
    fun deleteUserTest() {
        //given
        userRepository.save(User("A", 30))
        val name = userRepository.findAll()[0].name

        //when
        userService.deleteUserName(name)

        //when
        val result = userRepository.findAll()
        assertThat(result).isEmpty()
        assertThat(result).hasSize(0)
    }

    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다")
    fun getUserLoanHistoriesTest1() {
        //given
        userRepository.save(User("A", null))

        //when
        val results = userService.getUserLoanHistories()

        //then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작한다")
    fun getUserLoanHistoriesTest2() {
        //given
        val saverUser = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(saverUser, "책1", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(saverUser, "책2", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(saverUser, "책3", UserLoanStatus.RETURNED),
                UserLoanHistory.fixture(saverUser, "책4", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(saverUser, "책5", UserLoanStatus.RETURNED),
            )
        )

        //when
        val results = userService.getUserLoanHistories()

        //then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).hasSize(5)
        assertThat(results[0].books).extracting("name")
            .containsExactlyInAnyOrder("책1", "책2", "책3", "책4", "책5")
        assertThat(results[0].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false, false, true, false, true)
    }

}



















