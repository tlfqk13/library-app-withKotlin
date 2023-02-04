package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.user.loanhistory.User

interface UserRepositoryCustom {

    fun findAllWithHistories(): List<User>
}