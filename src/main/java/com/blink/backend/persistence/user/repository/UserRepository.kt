package com.blink.backend.persistence.user.repository

import com.blink.backend.persistence.entity.auth.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Int> {
    fun findAllByClinicCode(clinicCode: String): List<UserEntity>
}