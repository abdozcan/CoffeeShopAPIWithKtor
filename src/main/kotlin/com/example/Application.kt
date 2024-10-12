package com.example

import com.example.data.database.table.*
import com.example.data.repository.DefaultProductRepository
import com.example.data.repository.DefaultUserRepository
import com.example.data.utils.dbQuery
import com.example.domain.repository.ProductRepository
import com.example.domain.repository.UserRepository
import com.example.plugins.configureSecurity
import com.example.routes.productRoutes
import com.example.routes.userRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) { json() }
    configureDatabase()
    configureRouting()
    configureSecurity()
}

private fun Application.configureRouting() {
    productRoutes(DefaultProductRepository())
    userRoutes(DefaultUserRepository())
}

private fun Application.configureDatabase() {
    launch {
        transaction(
            Database.connect(
                "jdbc:sqlite:./data/coffee_shop_api.db",
                "org.sqlite.JDBC"
            )
        ) {
            SchemaUtils.create(
                AddressTable, CategoryTable, FavoriteTable,
                OrderItemTable, OrderTable, PaymentTable,
                PromotionTable, ProductTable, ReviewTable, UserTable
            )
        }
    }
}