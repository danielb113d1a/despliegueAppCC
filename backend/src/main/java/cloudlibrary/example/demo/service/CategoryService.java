package cloudlibrary.example.demo.service;

import cloudlibrary.example.demo.model.Category;
import cloudlibrary.example.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("La categoría ya existe");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updated) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(updated.getName());
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
