package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.Category;
import cloudlibrary.example.demo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class CategoryServiceIT {

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

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        // Limpiamos para que cada test sea independiente
        categoryRepository.deleteAll();
    }

    @Test
    void shouldCreateCategory() {
        Category category = new Category();
        category.setName("Ciencia Ficción");

        Category saved = categoryService.createCategory(category);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Ciencia Ficción");
    }

    @Test
    void shouldThrowWhenCreatingDuplicateCategory() {
        Category category1 = new Category();
        category1.setName("Historia");
        categoryService.createCategory(category1);

        Category category2 = new Category();
        category2.setName("Historia");

        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory(category2);
        });
    }

    @Test
    void shouldUpdateCategory() {
        Category category = new Category();
        category.setName("Terror");
        Category saved = categoryService.createCategory(category);

        Category updatedData = new Category();
        updatedData.setName("Horror Cósmico");

        Category updated = categoryService.updateCategory(saved.getId(), updatedData);

        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getName()).isEqualTo("Horror Cósmico");
    }
}