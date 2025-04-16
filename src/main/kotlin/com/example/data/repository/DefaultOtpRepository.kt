package com.example.data.repository

import com.example.data.database.dao.OtpEntity
import com.example.data.database.table.OtpTable
import com.example.data.utils.withTransactionContext
import com.example.domain.repository.OtpRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and

class DefaultOtpRepository : OtpRepository {
    override suspend fun find(email: String, otp: String): Result<String> = runCatching {
        withTransactionContext {
            OtpEntity.find((OtpTable.email eq email) and (OtpTable.otp eq otp)).single().token
        }
    }

    override suspend fun add(email: String, otp: String, token: String): Result<Unit> = runCatching {
        withTransactionContext {
            OtpEntity.find { OtpTable.email eq email }.map { it.delete() }
            OtpEntity.new {
                this.email = email
                this.otp = otp
                this.token = token
            }
        }
    }

    override suspend fun update(email: String, otp: String, token: String): Result<Unit> = runCatching {
        withTransactionContext {
            OtpEntity.findSingleByAndUpdate(OtpTable.email eq email) {
                it.otp = otp
                it.token = token
            }
        }
    }

    override suspend fun delete(email: String): Result<Unit> = runCatching {
        withTransactionContext {
            OtpEntity.find(OtpTable.email eq email).map { it.delete() }
        }
    }
}