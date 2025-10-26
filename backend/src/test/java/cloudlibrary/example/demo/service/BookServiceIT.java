package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class BookServiceIT { // "IT" = Integration Test

    // --- Configuración de Testcontainers ---
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    static {
        postgres.start();
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    // --- Inyectamos los Beans REALES ---
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository; // Necesario para preparar y verificar

    @BeforeEach
    void setup() {
        // Limpiamos la BBDD antes de CADA test para asegurar aislamiento
        bookRepository.deleteAll();
    }

    @Test
    void shouldSaveBook() {
        // given
        Book book = new Book(); // Usamos el constructor vacío
        book.setTitle("Refactoring");
        book.setAuthor("Martin Fowler");

        // when (Llamamos al servicio real)
        Book saved = bookService.saveBook(book);

        // then (Verificamos contra la BBDD real)
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Refactoring");
        assertThat(bookRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldFindAllBooks() {
        // given (Preparamos la BBDD)
        Book book1 = new Book();
        book1.setTitle("Clean Code");
        book1.setAuthor("Robert C. Martin");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Effective Java");
        book2.setAuthor("Joshua Bloch");
        bookRepository.save(book2);

        // when
        List<Book> books = bookService.findAllBooks();

        // then
        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getTitle).contains("Clean Code", "Effective Java");
    }

    @Test
    void shouldFindBookById() {
        // given
        Book book = new Book();
        book.setTitle("Effective Java");
        book.setAuthor("Joshua Bloch");
        Book saved = bookRepository.save(book);

        // when
        Optional<Book> found = bookService.findBookById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Effective Java");
    }

    @Test
    void shouldReturnEmptyOptionalForInvalidId() {
        // when
        Optional<Book> found = bookService.findBookById(999L);
        // then
        assertThat(found).isNotPresent();
    }

    @Test
    void shouldDeleteBook() {
        // given
        Book book = new Book();
        book.setTitle("To be deleted");
        book.setAuthor("Author");
        Book saved = bookRepository.save(book);
        Long id = saved.getId();

        // Verificación previa
        assertThat(bookRepository.existsById(id)).isTrue();

        // when
        bookService.deleteBook(id);

        // then (Verificamos que se borró)
        assertThat(bookRepository.existsById(id)).isFalse();
    }
}