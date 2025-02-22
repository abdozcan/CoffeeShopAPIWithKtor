package com.example.data.repository

import com.example.data.database.dao.AddressEntity
import com.example.data.database.table.AddressTable
import com.example.data.database.table.UserTable
import com.example.data.utils.doOrThrowIfNull
import com.example.data.utils.withTransactionContext
import com.example.domain.model.Address
import com.example.domain.repository.AddressRepository
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DefaultAddressRepository : AddressRepository {
    override suspend fun findDefaultByUserId(userId: Int): Result<Address> = runCatching {
        withTransactionContext {
            AddressEntity.find(
                (AddressTable.userId eq userId)
            ).first { it.isDefault }.toAddress()
        }
    }

    override suspend fun findAllByUserId(userId: Int): Result<List<Address>> = runCatching {
        withTransactionContext {
            AddressEntity.find {
                AddressTable.userId eq userId
            }.map {
                it.toAddress()
            }
        }
    }

    override suspend fun setDefaultAddress(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
            AddressEntity.findSingleByAndUpdate(AddressTable.isDefault eq true) { it.isDefault = false }
            AddressEntity.findByIdAndUpdate(id) { it.isDefault = true }
        }
    }

    override suspend fun add(name: String, userId: Int, address: String): Result<Address> = runCatching {
        withTransactionContext {
            AddressEntity.new {
                this.name = name
                this.userId = EntityID(userId, UserTable)
                this.address = address
                this.isDefault = false
            }.toAddress()
        }
    }

    override suspend fun edit(address: Address): Result<Unit> = runCatching {
        withTransactionContext {
            AddressEntity.findByIdAndUpdate(address.id) {
                it.name = address.name
                it.address = address.address
            }
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
            AddressEntity.findById(id).doOrThrowIfNull { it.delete() }
        }
    }
}