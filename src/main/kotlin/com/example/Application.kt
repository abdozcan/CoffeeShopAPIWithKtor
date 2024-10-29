package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.*
import com.example.auth.AuthManager
import com.example.auth.DefaultAuthManager
import com.example.data.database.table.*
import com.example.data.repository.*
import com.example.domain.repository.*
import com.example.routes.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.coroutines.launch
import org.jetbrains.exposed.exceptions.ExposedSQLException
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
    configureStatusPages()
    configureRouting(audience, issuer, secret)
}


private fun Application.configureStatusPages() = install(StatusPages) {
    exception<Throwable> { call, cause ->
        when (cause) {
            is NumberFormatException -> call.respond(HttpStatusCode.BadRequest, "Invalid parameter.")
            is NoSuchElementException -> call.respond(HttpStatusCode.NotFound, "Data not found.")
            is NotFoundException -> call.respond(HttpStatusCode.NotFound, cause.message.toString())
            is IllegalArgumentException,
            is NullPointerException,
            is MissingRequestParameterException,
            is BadRequestException,
            is ContentTransformationException,
            is AlgorithmMismatchException,
            is SignatureVerificationException,
            is TokenExpiredException,
            is MissingClaimException,
            is IncorrectClaimException,
            is ExposedSQLException,
            is JWTVerificationException -> call.respond(HttpStatusCode.BadRequest, cause.message.toString())

            else -> call.respond(HttpStatusCode.InternalServerError, "Something went wrong.")

        }
    }
}

private fun Application.configureRouting(audience: String, issuer: String, secret: String) {
    val productRepo: ProductRepository = DefaultProductRepository()
    val userRepo: UserRepository = DefaultUserRepository()
    val addressRepo: AddressRepository = DefaultAddressRepository()
    val authManager: AuthManager = DefaultAuthManager(audience, issuer, secret)
    val cartRepo: CartRepository = DefaultCartRepository()
    val favoriteRepo: FavoriteRepository = DefaultFavoriteRepository()
    val reviewRepo: ReviewRepository = DefaultReviewRepository()
    val orderRepo: OrderRepository = DefaultOrderRepository()
    val categoryRepo: CategoryRepository = DefaultCategoryRepository()


    productRoutes(productRepo, userRepo)
    userRoutes(userRepo)
    authRoutes(userRepo, authManager)
    cartRoutes(cartRepo)
    favoriteRoutes(favoriteRepo)
    reviewRoutes(reviewRepo)
    orderRoutes(orderRepo)
    addressRoutes(addressRepo)
    categoryRoutes(categoryRepo)
}

private fun Application.configureDatabase() {
    launch {
        transaction(
            Database.connect(
                "jdbc:sqlite:./data/coffee_shop_api.db" +
                        "?foreign_keys=on",
                "org.sqlite.JDBC"
            )
        ) {
            SchemaUtils.create(
                CategoryTable,
                ProductTable,
                UserTable,
                AddressTable,
                FavoriteTable,
                ReviewTable,
                CartItemTable,
                OrderTable,
                OrderItemTable,
                PaymentTable,
                PromotionTable
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