package cloudlibrary.example.demo.controller;

import cloudlibrary.example.demo.model.Category;
import cloudlibrary.example.demo.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        log.info("Request GET /api/categories - Solicitando todas las categorías");
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        log.info("Request GET /api/categories/{} - Solicitando categoría por ID", id);

        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("No se encontró la categoría con ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        log.info("Request POST /api/categories - Creando nueva categoría: {}", category.getName());
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        log.info("Request PUT /api/categories/{} - Actualizando categoría", id);
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Request DELETE /api/categories/{} - Eliminando categoría", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}