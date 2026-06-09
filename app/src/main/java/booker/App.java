package booker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("\n=======================================================");
        System.out.println("Booker Application Started Successfully!");
        System.out.println("Swagger UI is available at: http://localhost:8080/swagger-ui/index.html");
        System.out.println("API documentation is available at: http://localhost:8080/api-docs");
        System.out.println("=======================================================\n");
    }
}