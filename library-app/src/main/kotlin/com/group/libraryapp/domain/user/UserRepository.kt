package com.group.libraryapp.domain.user

import com.group.libraryapp.domain.user.loanhistory.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface UserRepository : JpaRepository<User,Long>, UserRepositoryCustom{

    fun findByName(name:String) : User?
}