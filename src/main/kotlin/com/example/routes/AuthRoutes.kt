package com.example.routes

import com.example.auth.AuthManager
import com.example.auth.model.Credential
import com.example.auth.model.RefreshToken
import com.example.auth.model.Tokens
import com.example.data.utils.Constant.OTP_LENGTH
import com.example.domain.model.OtpResponse
import com.example.domain.model.RegisterUser
import com.example.domain.model.VerificationOtpRequest
import com.example.domain.repository.OtpRepository
import com.example.domain.repository.UserRepository
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Tag(["Auth"])
fun Application.authRoutes(userRepo: UserRepository, otpRepo: OtpRepository, authManager: AuthManager) = routing {
    route("auth") {
        refreshToken(authManager)
        login(userRepo, authManager)
        register(userRepo, authManager)
        sendOtp(userRepo, otpRepo, authManager)
        verifyOtp(otpRepo, authManager)
        resendOtp(otpRepo, authManager)
    }
}

fun Route.refreshToken(authManager: AuthManager) = authenticate {
    post("/refresh-token") {
        call.receive<RefreshToken>().let { refreshToken ->
            val email: String = call.principal<JWTPrincipal>()?.payload?.getClaim("email")?.asString()!!

            authManager.verifyToken(
                token = refreshToken.refreshToken,
                email = email,
                type = "refresh"
            ).onSuccess {
                call.respond(
                    HttpStatusCode.OK,
                    Tokens(
                        accessToken = authManager.createToken(email = email, type = "access"),
                        refreshToken = authManager.createToken(email = email, type = "refresh")
                    )
                )
            }.onFailure {
                call.respond(HttpStatusCode.BadRequest, "Invalid token")
            }
        }
    }
}

fun Route.login(repo: UserRepository, authManager: AuthManager) = post("/login") {
    call.receive<Credential>().let { credential ->
        if (repo.isIncorrectCredential(credential))
            return@post call.respond(HttpStatusCode.Conflict, "Invalid email or password")

        call.respond(
            HttpStatusCode.OK,
            Tokens(
                accessToken = authManager.createToken(email = credential.email, type = "access"),
                refreshToken = authManager.createToken(email = credential.email, type = "refresh")
            )
        )
    }
}

fun Route.register(repo: UserRepository, authManager: AuthManager) = post("/register") {
    call.receive<RegisterUser>().let { user ->
        if (repo.isEmailUsed(user.email))
            return@post call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(error = "Email already used", type = ErrorType.EMAIL_USED)
            )
        if (repo.isPhoneUsed(user.phone))
            return@post call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(error = "Phone already used", type = ErrorType.PHONE_USED)
            )

        repo.add(
            user.name,
            user.password,
            user.email,
            user.phone
        ).getOrThrow().let { addedUser ->
            call.respond(
                HttpStatusCode.Created,
                Tokens(
                    accessToken = authManager.createToken(email = addedUser.email, type = "access"),
                    refreshToken = authManager.createToken(email = addedUser.email, type = "refresh")
                )
            )
        }
    }
}

fun Route.sendOtp(userRepo: UserRepository, otpRepo: OtpRepository, authManager: AuthManager) =
    post("/send-otp/{email?}") {
        call.parameters["email"]?.let { email ->
            userRepo.findByEmail(email)
                .onSuccess {
                    val otp = generateOtp()
                    val token = authManager.createToken(email = email, otp = otp, type = "reset")

                    otpRepo.add(email = email, otp = otp, token = token)
                        .onSuccess {
                            // todo: send otp to email
                            println("Otp: $otp")
                            call.respond(HttpStatusCode.OK, OtpResponse(length = OTP_LENGTH))
                        }.onFailure {
                            call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                        }
                }.onFailure {
                    call.respond(HttpStatusCode.NotFound, ErrorType.INVALID_EMAIL.name)
                }
        } ?: throw MissingRequestParameterException("email")
    }

fun Route.verifyOtp(otpRepo: OtpRepository, authManager: AuthManager) = post("/verify-otp") {
    call.receive<VerificationOtpRequest>().let { request ->
        otpRepo.find(email = request.email, otp = request.otp)
            .onSuccess { token ->
                authManager.verifyToken(
                    token = token,
                    email = request.email,
                    otp = request.otp,
                    type = "reset"
                ).onSuccess {
                    call.respond(HttpStatusCode.OK, "Otp verified successfully")
                }.onFailure {
                    call.respond(HttpStatusCode.BadRequest, "Invalid/Expired token")
                }
            }.onFailure {
                call.respond(HttpStatusCode.BadRequest, "Invalid otp")
            }
    }
}

fun Route.resendOtp(otpRepo: OtpRepository, authManager: AuthManager) =
    post("/resend-otp/{email?}") {
        call.parameters["email"]?.let { email ->
            val otp = generateOtp()
            val token = authManager.createToken(email = email, otp = otp, type = "reset")

            otpRepo.update(email = email, otp = otp, token = token)
                .onSuccess {
                    // todo: resend otp to email
                    println("Otp: $otp")
                    call.respond(HttpStatusCode.OK, "Otp resent successfully")
                }.onFailure {
                    call.respond(HttpStatusCode.InternalServerError, "Something went wrong")
                }
        } ?: throw MissingRequestParameterException("email")
    }

@Serializable
private data class ErrorResponse(
    val error: String,
    val type: ErrorType,
)

private enum class ErrorType {
    EMAIL_USED,
    PHONE_USED,
    INVALID_EMAIL
}

private fun generateOtp(length: Int = OTP_LENGTH) = (1..length).map { Random.nextInt(until = 10) }.joinToString("")