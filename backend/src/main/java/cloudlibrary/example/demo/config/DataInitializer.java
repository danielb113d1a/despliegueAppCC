package cloudlibrary.example.demo.config;

import cloudlibrary.example.demo.model.Category;
import cloudlibrary.example.demo.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            System.out.println("🚀 Base de datos vacía: Creando categorías por defecto...");

            createCategory("Ciencia Ficción");
            createCategory("Fantasía");
            createCategory("Terror");
            createCategory("Romance");
            createCategory("Tecnología");
            createCategory("Historia");
            createCategory("Desarrollo Personal");

            System.out.println("✅ Categorías creadas con éxito.");
        }
    }

    private void createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
    }
}