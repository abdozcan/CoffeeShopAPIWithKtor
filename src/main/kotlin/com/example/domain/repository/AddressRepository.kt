package com.example.domain.repository

import com.example.domain.model.Address

interface AddressRepository {
    suspend fun findDefaultByUserId(userId: Int): Result<Address>
    suspend fun findAllByUserId(userId: Int): Result<List<Address>>
    suspend fun setDefaultAddress(id: Int): Result<Unit>
    suspend fun add(name: String, userId: Int, address: String): Result<Address>
    suspend fun edit(address: Address): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
}