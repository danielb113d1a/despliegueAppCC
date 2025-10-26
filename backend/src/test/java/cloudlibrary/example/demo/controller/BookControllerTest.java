package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @InjectMocks
    private BookController bookController; // Controlador real

    @Mock
    private BookService bookService; // Mock del servicio

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Clean Code");
        testBook.setAuthor("Robert C. Martin");
        testBook.setDescription("Best practices for writing clean, maintainable code.");
    }

    @Test
    void shouldReturnAllBooks() {
        when(bookService.findAllBooks()).thenReturn(List.of(testBook));

        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("Clean Code");

        verify(bookService, times(1)).findAllBooks();
    }

    @Test
    void shouldReturnBookById() {
        when(bookService.findBookById(1L)).thenReturn(Optional.of(testBook));

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(testBook);

        verify(bookService, times(1)).findBookById(1L);
    }

    @Test
    void shouldAddNewBook() {
        when(bookService.saveBook(Mockito.any(Book.class))).thenReturn(testBook);

        ResponseEntity<Book> response = bookController.addBook(testBook);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(testBook);

        verify(bookService, times(1)).saveBook(Mockito.any(Book.class));
    }

    @Test
    void shouldDeleteBook() {
        // No devuelve nada, solo verificamos que se llame correctamente
        doNothing().when(bookService).deleteBook(1L);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        verify(bookService, times(1)).deleteBook(1L);
    }
}
