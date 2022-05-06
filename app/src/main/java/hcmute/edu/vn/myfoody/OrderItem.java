package hcmute.edu.vn.myfoody;

public class OrderItem {
    Integer OrderId, StoreId;
    String CreateTime, Email;
    Float Total;

    public OrderItem(Integer orderId, Integer storeId, String createTime, String email, Float total) {
        OrderId = orderId;
        StoreId = storeId;
        CreateTime = createTime;
        Email = email;
        Total = total;
    }

    public Integer getOrderId() { return OrderId; }

    public void setOrderId(Integer orderId) { OrderId = orderId; }

    public Integer getStoreId() {
        return StoreId;
    }

    public void setStoreId(Integer storeId) {
        StoreId = storeId;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Float getTotal() {
        return Total;
    }

    public void setTotal(Float total) {
        Total = total;
    }
}

