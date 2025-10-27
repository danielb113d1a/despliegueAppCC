package cloudlibrary.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration; // Asegúrate de importar estas
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration; // Asegúrate de importar estas
import org.springframework.boot.test.context.SpringBootTest;

// ESTA PARTE ES LA IMPORTANTE
@SpringBootTest
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
class DemoApplicationTests {

    @Test
    void contextLoads() {
        // Este test ahora solo comprueba que el contexto básico carga,
        // sin intentar conectar a ninguna base de datos.
    }

}