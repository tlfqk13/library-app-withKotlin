package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.User
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserLoanHistoryResponse
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.util.fail
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository // 불변
) {

    @Transactional
    fun saveUser(request: UserCreateRequest) {
        val newUser = User(request.name, request.age)
        userRepository.save(newUser)
    }

    @Transactional(readOnly = true)
    fun getUsers(): List<UserResponse> {
        return userRepository.findAll()
            .map { user -> UserResponse.of(user) }
    }

    @Transactional
    fun updateUserName(request: UserUpdateRequest) {
        val updateUser = userRepository.findByIdOrNull(request.id) ?: fail()
        updateUser.updateName(request.name)
    }

    @Transactional
    fun deleteUserName(name: String) {
        val deleteUser = userRepository.findByName(name) ?: fail()
        userRepository.delete(deleteUser)
    }

    @Transactional(readOnly = true)
    fun getUserLoanHistories(): List<UserLoanHistoryResponse> {
        return userRepository.findAllWithHistories()
            .map(UserLoanHistoryResponse::of)
        /* UserLoanHistoryResponse(
             name = user.name,
             *//*books = user.userLoanHistories.map { history ->
                   BookHistoryResponse.of(history)
                }*//*
                books =user.userLoanHistories.map(BookHistoryResponse::of)
            )*/
    }
}
