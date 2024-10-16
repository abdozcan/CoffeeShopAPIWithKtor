package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.auth.DefaultAuthManager
import com.example.data.database.table.*
import com.example.data.repository.DefaultProductRepository
import com.example.data.repository.DefaultUserRepository
import com.example.routes.authRoutes
import com.example.routes.productRoutes
import com.example.routes.userRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
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
    val (audience, issuer, secret) = configureSecurity()
    configureRouting(audience, issuer, secret)
}

private fun Application.configureRouting(audience: String, issuer: String, secret: String) {
    productRoutes(DefaultProductRepository())
    userRoutes(DefaultUserRepository())
    authRoutes(
        DefaultUserRepository(),
        DefaultAuthManager(audience, issuer, secret)
    )
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

private fun Application.configureSecurity(): Triple<String, String, String> {
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtDomain = environment.config.property("jwt.domain").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()

    authentication {
        jwt {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
    return Triple(jwtAudience, jwtIssuer, jwtSecret)
}