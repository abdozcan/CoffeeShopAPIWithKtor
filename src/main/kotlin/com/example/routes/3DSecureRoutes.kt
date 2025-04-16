package com.example.routes

import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*

@Tag(["3D Secure"])
fun Application.`3DSecureRoutes`() = routing {
    `3DSecureVerification`()
    `3DSecureCallback`()
    `3DSecureSuccess`()
    `3DSecureFailure`()
}

private fun Route.`3DSecureVerification`() = get("3d-secure/verification") {
    call.respondHtml {
        head {
            title { +"3D Secure Verification" }
            style {
                +"""
                            body {
                                font-family: Arial, sans-serif;
                                background-color: #f4f4f4;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                height: 100vh;
                                margin: 0;
                            }
                            .container {
                                background-color: #fff;
                                padding: 20px;
                                margin-top: 250px;
                                border-radius: 8px;
                                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                                width: 300px;
                                text-align: center;
                            }
                            input[type="text"] {
                                width: 100%;
                                padding: 10px;
                                border: 1px solid #ccc;
                                border-radius: 4px;
                            }
                            label {
                                margin: 10px 0;
                            }
                            button {
                                background-color: #007bff;
                                color: white;
                                padding: 10px 20px;
                                margin: 10px 0;
                                border: none;
                                border-radius: 4px;
                                cursor: pointer;
                            }
                            button:hover {
                                background-color: #0056b3;
                            }
                        """.trimIndent()
            }
        }
        body {
            div("container") {
                h1 { +"3D Secure Verification" }
                form(action = "/3d-secure/callback", method = FormMethod.post) {
                    label {
                        +"Enter verification code:"
                    }
                    br
                    input(type = InputType.text, name = "code") {
                        placeholder = "Test Code: 1234"
                    }
                    br
                    button(type = ButtonType.submit) {
                        +"Submit"
                    }
                }
            }
        }
    }
}

private fun Route.`3DSecureCallback`() = post("3d-secure/callback") {
    call.receiveParameters()["code"].let { code ->
        if (code == "1234") call.respondRedirect("/3d-secure/success")
        else call.respondRedirect("/3d-secure/failure")
    }
}

private fun Route.`3DSecureSuccess`() = get("3d-secure/success") {
    call.respondHtml {
        head {
            title { +"3D Secure Success" }
            style {
                +"""
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        margin: 0;
                    }
                    .container {
                        background-color: #fff;
                        padding: 20px;
                        margin-top: 250px;
                        border-radius: 8px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                        width: 300px;
                        text-align: center;
                    }
                    h1 {
                        color: #4CAF50;
                    }
                """.trimIndent()
            }
        }
        body {
            div("container") {
                h1 { +"3D Secure Verification Successful!" }
                p { +"You can now complete your purchase." }
            }
        }
    }
}

private fun Route.`3DSecureFailure`() = get("3d-secure/failure") {
    call.respondHtml {
        head {
            title { +"3D Secure Failure" }
            style {
                +"""
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        margin: 0;
                    }
                    .container {
                        background-color: #fff;
                        padding: 20px;
                        margin-top: 250px;
                        border-radius: 8px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                        width: 300px;
                        text-align: center;
                    }
                    h1 {
                        color: #FF0000;
                    }
                """.trimIndent()
            }
        }
        body {
            div("container") {
                h1 { +"3D Secure Verification Failed!" }
                p { +"Please try again." }
            }
        }
    }
}