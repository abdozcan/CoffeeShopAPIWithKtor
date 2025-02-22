package com.example.data.database.table

import org.jetbrains.exposed.dao.id.IntIdTable

object FaqTranslationTable : IntIdTable("faq_translations") {
    val faqId = reference("faq_id", FaqTable)
    val languageCode = varchar("language_code", 2) // ISO 639-1 (2-letter language code)
    val question = text("question")
    val answer = text("answer")
    val category = varchar("category", 255).nullable()
}