package com.kalasalingam.canteen.dto;

public class CartItemDTO {

    private Long foodItemId;
    private Integer quantity;

    public CartItemDTO() {}

    public CartItemDTO(Long foodItemId, Integer quantity) {
        this.foodItemId = foodItemId;
        this.quantity = quantity;
    }

    public Long getFoodItemId() { return foodItemId; }
    public void setFoodItemId(Long foodItemId) { this.foodItemId = foodItemId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
