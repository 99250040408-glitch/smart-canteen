package com.kalasalingam.canteen.service;

import com.kalasalingam.canteen.entity.FoodItem;
import com.kalasalingam.canteen.repository.FoodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    public FoodItemService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    public List<FoodItem> findAll() {
        return foodItemRepository.findAll();
    }

    public List<FoodItem> findAvailable() {
        return foodItemRepository.findByAvailableTrue();
    }

    public List<FoodItem> findByCategory(String category) {
        return foodItemRepository.findByCategoryAndAvailableTrue(category);
    }

    public List<FoodItem> search(String keyword) {
        return foodItemRepository.findByNameContainingIgnoreCaseAndAvailableTrue(keyword);
    }

    public Optional<FoodItem> findById(Long id) {
        return foodItemRepository.findById(id);
    }

    public FoodItem save(FoodItem item) {
        return foodItemRepository.save(item);
    }

    public void deleteById(Long id) {
        foodItemRepository.deleteById(id);
    }

    public List<String> getAllCategories() {
        return foodItemRepository.findDistinctCategories();
    }
}
