package com.kalasalingam.canteen.repository;

import com.kalasalingam.canteen.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByAvailableTrue();
    List<FoodItem> findByCategory(String category);
    List<FoodItem> findByCategoryAndAvailableTrue(String category);
    List<FoodItem> findByNameContainingIgnoreCaseAndAvailableTrue(String name);

    @Query("SELECT DISTINCT f.category FROM FoodItem f ORDER BY f.category")
    List<String> findDistinctCategories();
}
