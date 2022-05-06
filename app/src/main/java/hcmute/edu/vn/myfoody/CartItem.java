package hcmute.edu.vn.myfoody;

public class CartItem {
    Integer FoodId;
    String Email;
    Integer Quantity;

    public CartItem(Integer foodId, String email, Integer quantity) {
        FoodId = foodId;
        Email = email;
        Quantity = quantity;
    }

    public Integer getFoodId() {
        return FoodId;
    }

    public void setFoodId(Integer foodId) {
        FoodId = foodId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }
}
