package com.android.CRUDSQLite

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [BookEntity::class, AuthorEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract val booksDao: BooksDao
    abstract val authorsDao: AuthorsDao

    companion object {
        const val DATABASE_NAME = "db-Libros"
    }
}