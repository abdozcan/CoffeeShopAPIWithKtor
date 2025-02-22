package com.example.data.database.dao

import com.example.data.database.table.FaqTranslationTable
import com.example.domain.model.FaqTranslation
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID

class FaqTranslationEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : EntityClass<Int, FaqTranslationEntity>(FaqTranslationTable)

    var faqId by FaqTranslationTable.faqId
    var languageCode by FaqTranslationTable.languageCode
    var question by FaqTranslationTable.question
    var answer by FaqTranslationTable.answer
    var category by FaqTranslationTable.category

    fun toFaqTranslation() = FaqTranslation(
        id = faqId.value,
        faqId = faqId.value,
        languageCode = languageCode,
        question = question,
        answer = answer,
        category = category
    )
}