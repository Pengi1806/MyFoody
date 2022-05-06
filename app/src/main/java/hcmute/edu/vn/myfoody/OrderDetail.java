package hcmute.edu.vn.myfoody;

public class OrderDetail {
    private Integer OrderId, FoodId, Quantity;
    private Float UnitPrice;

    public OrderDetail(Integer orderId, Integer foodId, Integer quantity, Float unitPrice) {
        OrderId = orderId;
        FoodId = foodId;
        Quantity = quantity;
        UnitPrice = unitPrice;
    }

    public Integer getOrderId() {
        return OrderId;
    }

    public void setOrderId(Integer orderId) {
        OrderId = orderId;
    }

    public Integer getFoodId() {
        return FoodId;
    }

    public void setFoodId(Integer foodId) {
        FoodId = foodId;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public Float getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        UnitPrice = unitPrice;
    }
}
