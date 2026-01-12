package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Book;
import cloudlibrary.example.demo.model.User;
import cloudlibrary.example.demo.service.BookService;
import cloudlibrary.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    private Book testBook;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@test.com");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Clean Code");
        testBook.setAuthor("Robert C. Martin");
        testBook.setDescription("Best practices...");
        testBook.setUser(testUser);
    }

    @Test
    void shouldReturnAllBooks() {
        when(bookService.findAllBooks()).thenReturn(List.of(testBook));

        List<Book> response = bookController.getAllBooks();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getTitle()).isEqualTo("Clean Code");

        verify(bookService, times(1)).findAllBooks();
    }

    @Test
    void shouldCreateBook() {
        Map<String, Object> bookData = new HashMap<>();
        bookData.put("title", "Clean Code");
        bookData.put("author", "Robert C. Martin");
        bookData.put("description", "Best practices...");
        bookData.put("email", "test@test.com");

        when(userService.getUserByEmail("test@test.com")).thenReturn(testUser);

        when(bookService.saveBook(any(Book.class))).thenReturn(testBook);

        ResponseEntity<?> response = bookController.createBook(bookData);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        verify(userService, times(1)).getUserByEmail("test@test.com");
        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    void shouldDeleteBook() {
        doNothing().when(bookService).deleteBook(1L);

        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        verify(bookService, times(1)).deleteBook(1L);
    }
}