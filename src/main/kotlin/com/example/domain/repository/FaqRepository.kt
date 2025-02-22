package com.example.domain.repository

import com.example.domain.model.FaqTranslation

interface FaqRepository {
    suspend fun findAll(): Result<List<FaqTranslation>>
    suspend fun findAllByLanguageCode(code: String): Result<List<FaqTranslation>>
    suspend fun findTranslationsByFaqId(faqId: Int): Result<List<FaqTranslation>>
    suspend fun addFaq(faq: FaqTranslation): Result<FaqTranslation>
    suspend fun addFaqTranslation(faq: FaqTranslation): Result<FaqTranslation>
    suspend fun edit(faq: FaqTranslation): Result<Unit>
    suspend fun delete(id: Int): Result<Unit>
}