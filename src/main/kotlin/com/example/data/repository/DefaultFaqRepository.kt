package com.example.data.repository

import com.example.data.database.dao.FaqEntity
import com.example.data.database.dao.FaqTranslationEntity
import com.example.data.database.table.FaqTable
import com.example.data.database.table.FaqTranslationTable
import com.example.data.utils.withTransactionContext
import com.example.domain.model.FaqTranslation
import com.example.domain.repository.FaqRepository
import org.jetbrains.exposed.dao.id.EntityID

class DefaultFaqRepository : FaqRepository {
    override suspend fun findAll(): Result<List<FaqTranslation>> = runCatching {
        withTransactionContext {
            FaqTranslationEntity.all().map { it.toFaqTranslation() }
        }
    }

    override suspend fun findAllByLanguageCode(code: String): Result<List<FaqTranslation>> = runCatching {
        withTransactionContext {
            FaqTranslationEntity.find {
                FaqTranslationTable.languageCode eq code
            }.map {
                it.toFaqTranslation()
            }
        }
    }

    override suspend fun findTranslationsByFaqId(faqId: Int): Result<List<FaqTranslation>> = runCatching {
        withTransactionContext {
            FaqTranslationEntity.find { FaqTranslationTable.faqId eq faqId }.map { it.toFaqTranslation() }
        }
    }

    override suspend fun addFaq(faq: FaqTranslation): Result<FaqTranslation> = runCatching {
        withTransactionContext {
            val newFaq = FaqEntity.new { }

            FaqTranslationEntity.new {
                this.faqId = newFaq.id
                this.languageCode = faq.languageCode
                this.question = faq.question
                this.answer = faq.answer
                this.category = faq.category
            }.toFaqTranslation()
        }
    }

    override suspend fun addFaqTranslation(faq: FaqTranslation): Result<FaqTranslation> = runCatching {
        withTransactionContext {
            FaqTranslationEntity.new {
                this.faqId = EntityID(faq.faqId, FaqTable)
                this.languageCode = faq.languageCode
                this.question = faq.question
                this.answer = faq.answer
                this.category = faq.category
            }.toFaqTranslation()
        }
    }

    override suspend fun edit(faq: FaqTranslation): Result<Unit> = runCatching {
        withTransactionContext {
            FaqTranslationEntity.findByIdAndUpdate(id = faq.id) {
                it.answer = faq.answer
                it.question = faq.question
                it.category = faq.category
            }
        }
    }

    override suspend fun delete(id: Int): Result<Unit> = runCatching {
        withTransactionContext {
            FaqTranslationEntity.findById(id)?.delete()
        }
    }
}