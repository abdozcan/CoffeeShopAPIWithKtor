package com.example.data.utils

import io.ktor.server.plugins.*

/**
 * @throws NotFoundException
 */
inline fun <T, R: Any> T?.doOrThrowIfNull(transform: (T) -> R): R {
    return this?.let(transform) ?: throw NotFoundException("Not Found.")
}

/**
 * @throws NoSuchElementException
 */
inline fun <T, R> Iterable<T>.mapOrTrowIfEmpty(transform: (T) -> R): List<R> {
    if (this.none()) {
        throw NoSuchElementException("The collection is empty.")
    }
    return this.map(transform)
}

inline fun <T, R> Iterable<T>.flatMapOrTrowIfEmpty(transform: (T) -> Iterable<R>): List<R> {
    if (this.none()) {
        throw NoSuchElementException("The collection is empty.")
    }
    return this.flatMap(transform)
}