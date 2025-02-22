package com.example.routes

import com.example.domain.model.FaqTranslation
import com.example.domain.repository.FaqRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.faqRoutes(repo: FaqRepository) = routing {
    authenticate {
        getAll(repo)
        getTranslationsByFaqId(repo)
        addFaq(repo)
        addFaqTranslation(repo)
        edit(repo)
        delete(repo)
    }
    getAllByLanguageCode(repo)
}

private fun Route.getAll(repo: FaqRepository) = get("/faq/all") {
    call.respond(HttpStatusCode.OK, repo.findAll().getOrThrow())
}

private fun Route.getAllByLanguageCode(repo: FaqRepository) = get("/faq/language_code/{code?}") {
    call.parameters["code"]?.let { code ->
        repo.findAllByLanguageCode(code).getOrThrow().let { faqs ->
            call.respond(HttpStatusCode.OK, faqs)
        }
    } ?: throw MissingRequestParameterException("language code")
}

private fun Route.getTranslationsByFaqId(repo: FaqRepository) = get("/faq/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.findTranslationsByFaqId(id).getOrThrow().let { faqs ->
            call.respond(HttpStatusCode.OK, faqs)
        }
    } ?: throw MissingRequestParameterException("id")
}

private fun Route.addFaq(repo: FaqRepository) = post("/faq/add_faq") {
    call.receive<FaqTranslation>().let { request ->
        repo.addFaq(request).getOrThrow().let { faq ->
            call.respond(HttpStatusCode.OK, faq)
        }
    }
}

private fun Route.addFaqTranslation(repo: FaqRepository) = post("/faq/add_translation") {
    call.receive<FaqTranslation>().let { request ->
        repo.addFaqTranslation(request).getOrThrow().let { faq ->
            call.respond(HttpStatusCode.OK, faq)
        }
    }
}

private fun Route.edit(repo: FaqRepository) = put("/faq/edit") {
    call.receive<FaqTranslation>().let { request ->
        repo.edit(request).getOrThrow().let { faq ->
            call.respond(HttpStatusCode.OK, "Edited successfully")
        }
    }
}

private fun Route.delete(repo: FaqRepository) = delete("/faq/delete/{id?}") {
    call.parameters["id"]?.toInt()?.let { id ->
        repo.delete(id).getOrThrow().let { faq ->
            call.respond(HttpStatusCode.OK, "Deleted successfully")
        }
    } ?: throw MissingRequestParameterException("id")
}