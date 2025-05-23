package com.example.data.repository

import com.example.auth.model.Credential
import com.example.data.database.dao.UserEntity
import com.example.data.database.table.UserTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.withTransactionContext
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

class DefaultUserRepository : UserRepository {
    override suspend fun findByEmail(email: String): Result<User> = runCatching {
        withTransactionContext {
            UserEntity.find {
                UserTable.email eq email
            }.single().toUser()
        }
    }

    override suspend fun findById(id: Int): Result<User> = runCatching {
        withTransactionContext {
            UserEntity.findById(id).doOrThrowIfNull { it.toUser() }
        }
    }

    override suspend fun isEmailUsed(email: String): Boolean = withTransactionContext {
        UserEntity.find { UserTable.email eq email }.none().not()
    }

    override suspend fun isPhoneUsed(phone: String): Boolean = withTransactionContext {
        UserEntity.find { UserTable.phone eq phone }.none().not()
    }

    override suspend fun isIncorrectCredential(credential: Credential): Boolean = withTransactionContext {
        UserEntity.find {
            (UserTable.email eq credential.email) and (UserTable.password eq credential.password)
        }.none()
    }

    override suspend fun add(
        name: String,
        password: String,
        email: String,
        phone: String
    ): Result<User> = runCatching {
        withTransactionContext {
            UserEntity.new {
                this.name = name
                this.password = password
                this.email = email
                this.phone = phone
            }.toUser()
        }
    }

    override suspend fun edit(user: User): Result<Unit> = runCatching {
        withTransactionContext {
            UserEntity.findByIdAndUpdate(user.id) {
                it.name = user.name
                it.email = user.email
                it.phone = user.phone
            }
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
            UserEntity.findById(id).doOrThrowIfNull { it.delete() }
        }
    }

    override suspend fun changePassword(userId: Int, oldPassword: String, newPassword: String): Result<Unit> =
        runCatching {
            withTransactionContext {
                UserEntity.findByIdAndUpdate(userId) {
                    if (it.password == oldPassword) it.password = newPassword
                    else throw IllegalArgumentException("Old password is incorrect")
                }
            }
        }

    override suspend fun resetPassword(email: String, password: String): Result<Unit> = runCatching {
        withTransactionContext {
            UserEntity.findSingleByAndUpdate(UserTable.email eq email) {
                it.password = password
            }
        }
    }
}