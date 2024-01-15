package com.yariv.ofek.taxicounter.domain.use_case

interface LanguageUseCase {
    fun getLanguage(): String
}

class LanguageUseCaseImpl : LanguageUseCase {
    override fun getLanguage(): String {
        return "en"
    }
}