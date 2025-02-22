package com.example.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FaqTranslation(
    val id: Int,
    @SerialName("faq_id")
    val faqId: Int,
    @SerialName("language_code")
    val languageCode: String,
    val question: String,
    val answer: String,
    val category: String?
)