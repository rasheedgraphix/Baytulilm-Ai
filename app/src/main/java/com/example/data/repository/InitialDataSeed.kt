package com.example.data.repository

import com.example.data.model.BookEntity

object InitialDataSeed {
    val sampleBooks: List<BookEntity> by lazy {
        InitialDataSeedPart1.books +
        InitialDataSeedPart2.books +
        InitialDataSeedPart3.books +
        InitialDataSeedPart4.books +
        InitialDataSeedPart5.books +
        InitialDataSeedPart6.books +
        InitialDataSeedPart7.books
    }
}
