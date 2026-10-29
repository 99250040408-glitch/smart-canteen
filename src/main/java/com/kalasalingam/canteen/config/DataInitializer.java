package com.kalasalingam.canteen.config;

import com.kalasalingam.canteen.entity.FoodItem;
import com.kalasalingam.canteen.entity.User;
import com.kalasalingam.canteen.repository.FoodItemRepository;
import com.kalasalingam.canteen.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, FoodItemRepository foodItemRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.foodItemRepository = foodItemRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Seed users if empty
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@kare.edu");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);

            User student = new User();
            student.setName("Rahul Kumar");
            student.setEmail("student@kare.edu");
            student.setPasswordHash(passwordEncoder.encode("student123"));
            student.setRole("STUDENT");
            userRepository.save(student);

            System.out.println("==> Seeded 2 users (admin@kare.edu / admin123, student@kare.edu / student123)");
        }

        // Seed food items if empty
        if (foodItemRepository.count() == 0) {
            List<FoodItem> items = List.of(
                createItem("Idli",          30.0,  "Breakfast",  "/images/idli.jpg"),
                createItem("Masala Dosa",    50.0,  "Breakfast",  "/images/masala_dosa.jpg"),
                createItem("Pongal",         40.0,  "Breakfast",  "/images/pongal.jpg"),
                createItem("Veg Biryani",    90.0,  "Meals",      "/images/veg_biryani.jpg"),
                createItem("Parotta",        35.0,  "Meals",      "/images/parotta.jpg"),
                createItem("Veg Meals",     100.0,  "Meals",      "/images/veg_meals.jpg"),
                createItem("Fried Rice",     80.0,  "Meals",      "/images/fried_rice.jpg"),
                createItem("Samosa",         20.0,  "Snacks",     "/images/samosa.jpg"),
                createItem("Vada",           25.0,  "Snacks",     "/images/vada.jpg"),
                createItem("Tea",            15.0,  "Beverages",  "/images/tea.jpg"),
                createItem("Coffee",         20.0,  "Beverages",  "/images/coffee.jpg"),
                createItem("Lemon Juice",    25.0,  "Beverages",  "/images/lemon_juice.jpg")
            );
            foodItemRepository.saveAll(items);
            System.out.println("==> Seeded 12 food items");
        }
    }

    private FoodItem createItem(String name, double price, String category, String imageUrl) {
        FoodItem item = new FoodItem();
        item.setName(name);
        item.setPrice(price);
        item.setCategory(category);
        item.setAvailable(true);
        item.setImageUrl(imageUrl);
        return item;
    }
}
