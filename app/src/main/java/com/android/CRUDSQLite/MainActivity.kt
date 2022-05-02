package com.android.CRUDSQLite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.room.Room

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private val books: ArrayList<BookEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        database = Room.databaseBuilder(
            application, AppDatabase::class.java, AppDatabase.DATABASE_NAME
        )
            .addMigrations(Migrations.MIGRATION_1_2)
            .allowMainThreadQueries()
            .build()

        btnBooks.setOnClickListener {
            val books = database.booksDao.getAllBooks()
            tvBooks.text = "" // This will reset the text of TextView
            books.forEach { book ->
                tvBooks.append("${book.id}, ${book.title}, ${book.author}, ${book.pubDate}\n")
            }
            this.books.clear()
            this.books.addAll(books)
        }
    }

    private fun saveBooks() {
        val book1 = BookEntity(title = "Moby Dick", author = "Herman Melville", pubDate = 1851, price =9.89 )
        val book2 = BookEntity(title = "Taipi", author = "Herman Melville", pubDate = 1846, price = 7.00)
        val book3 = BookEntity(title = "Don Quijote", author = "Miguel de Cervantes", pubDate = 1605, price = 7.99)
        val book4 = BookEntity(title = "Harry Potter y la piedra filosofal", author = "J. K. Rowling", pubDate = 1997, price = 11.57)
        val book5 = BookEntity(title = "Harry Potter y la cámara secreta", author = "J. K. Rowling", pubDate = 1998, price=11.53)
        val book6 = BookEntity(title = "Harry Potter y el cáliz de fuego", author = "J. K. Rowling", pubDate = 2001,price=16.39)
        val book7 = BookEntity(title = "Harry Potter y la Orden del Fénix", author = "J. K. Rowling", pubDate = 2003,price=17.50)
        val book8 = BookEntity(title = "Veinte Poemas de amor y cancion desesperada", author = "Pablo Neruda", pubDate = 1924, price=14.59)
        val book9 = BookEntity(title = "Un cadaver en la biblioteca", author = "Agatha Christie", pubDate = 2003, price=7.95)
        database.booksDao.insert(book1)
        database.booksDao.insert(book2)
        database.booksDao.insert(book3)
        database.booksDao.insert(book4)
        database.booksDao.insert(book5)
        database.booksDao.insert(book6)
        database.booksDao.insert(book7)
        database.booksDao.insert(book8)
        database.booksDao.insert(book9)
    }

    //  This will create the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_insert_all -> saveBooks()
            R.id.action_update -> updateFirstBook()
            R.id.action_delete -> deleteFirstBook()
            R.id.action_delete_all -> deleteAll()
            R.id.action_books_by_author -> getBooksByAuthor()
            R.id.action_all_titles -> getAllTitles()
            R.id.action_insert_authors -> saveAuthors()
            R.id.action_get_authors_and_books -> showAuthorsAndTheirBooks()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateFirstBook() {
        if (books.size > 0) {
            books.first().title = "Otro libro"
            database.booksDao.update(books.first())
        }
    }

    private fun deleteFirstBook() {
        if (books.size > 0)
            database.booksDao.delete(books.first())
    }

    // This method is provided by Room
    private fun deleteAll() {
        database.clearAllTables()
    }

    private fun getBooksByAuthor() {
        val books = database.booksDao.getBooksByAuthor("J. K. Rowling")
        tvBooks.text = ""
        books.forEach { book ->
            tvBooks.append("${book.id}, ${book.title}, ${book.author}, ${book.pubDate}\n")
        }
    }

    private fun getAllTitles() {
        val titles = database.booksDao.getAllTitles()
        tvBooks.text = ""
        titles.forEach { title ->
            tvBooks.append("$title\n")
        }
    }

    private fun saveAuthors() {
        val authors = ArrayList<AuthorEntity>().apply {
            add(AuthorEntity("Herman Melville", 1819, 2))
            add(AuthorEntity("Miguel de Cervantes", 1547, 1))
            add(AuthorEntity("Pablo Neruda", 1904, 1))
            add(AuthorEntity("Agatha Christie", 1890, 1))
            add(AuthorEntity("J. K. Rowling",1965,4))
        }
        database.authorsDao.insertMany(authors)
    }

    private fun showAuthorsAndTheirBooks() {
        val authors = database.authorsDao.getAuthorsWithMoreThanXAmountOfBooks(1)
        tvBooks.text = ""
        authors.forEach { author ->
            tvBooks.append("${author.name}, ${author.yearOfBirth}, ${author.numberOfBooks}\n")
        }
        if (authors.isEmpty()) return
        val booksByAuthor = database.booksDao.getBooksByAuthor(authors[0].name)
        booksByAuthor.forEach { book ->
            tvBooks.append("${book.id}, ${book.title}, ${book.author}, ${book.pubDate}\n")
        }
    }
}