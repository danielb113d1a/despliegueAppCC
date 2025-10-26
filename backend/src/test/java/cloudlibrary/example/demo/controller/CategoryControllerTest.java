package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Category;
import cloudlibrary.example.demo.service.CategoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Fantasía");
    }

    @Test
    void shouldGetAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(List.of(testCategory));

        ResponseEntity<List<Category>> response = categoryController.getAllCategories();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void shouldGetCategoryById() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(testCategory));

        ResponseEntity<Category> response = categoryController.getCategoryById(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getName()).isEqualTo("Fantasía");
        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void shouldReturnNotFoundForInvalidId() {
        when(categoryService.getCategoryById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Category> response = categoryController.getCategoryById(99L);

        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
        verify(categoryService, times(1)).getCategoryById(99L);
    }

    @Test
    void shouldCreateCategory() {
        when(categoryService.createCategory(any(Category.class))).thenReturn(testCategory);

        ResponseEntity<Category> response = categoryController.createCategory(testCategory);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(testCategory);
        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void shouldUpdateCategory() {
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(testCategory);

        ResponseEntity<Category> response = categoryController.updateCategory(1L, testCategory);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(testCategory);
        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void shouldDeleteCategory() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(categoryService, times(1)).deleteCategory(1L);
    }
}