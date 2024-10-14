package com.example.data.repository

import com.example.data.database.dao.UserEntity
import com.example.data.database.table.UserTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.withTransactionContext
import com.example.domain.model.User
import com.example.domain.repository.UserRepository

class DefaultUserRepository : UserRepository {
    override suspend fun findByEmail(email: String): Result<User> = runCatching {
        withTransactionContext {
            UserEntity.find {
                UserTable.email eq email
            }.single()
                .doOrThrowIfNull { it.toUser() }
        }
    }

    override suspend fun add(
        name: String,
        password: String,
        email: String,
        phone: String,
        address: String
    ): Result<User> = runCatching {
        withTransactionContext {
            UserEntity.new {
                this.name = name
                this.password = password
                this.email = email
                this.phone = phone
                this.defaultAddress = address
            }.toUser()
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
            UserEntity.findById(id).doOrThrowIfNull { it.delete() }
        }
    }

    override suspend fun selectDefaultAddress(userId: Int, address: String): Result<User> = runCatching {
        withTransactionContext {
            UserEntity.findByIdAndUpdate(userId) {
                it.defaultAddress = address
            }.doOrThrowIfNull { it.toUser() }
        }
    }
}