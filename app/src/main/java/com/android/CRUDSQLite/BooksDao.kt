package com.android.CRUDSQLite

import androidx.room.*


@Dao
interface BooksDao {

    @Insert
    fun insert(book: BookEntity)

    @Query("SELECT * FROM books")
    fun getAllBooks(): List<BookEntity>

    @Update
    fun update(book: BookEntity)

    @Delete
    fun delete(book: BookEntity)

    @Query("SELECT * FROM books WHERE author = :author")
    fun getBooksByAuthor(author: String): List<BookEntity>

    @Query("SELECT title FROM books")
    fun getAllTitles(): List<String>
}