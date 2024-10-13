package com.example.data.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> withTransactionContext(dispatcher: CoroutineDispatcher = Dispatchers.IO, block: suspend () -> T): T =
    newSuspendedTransaction(dispatcher) { block() }