package hcmute.edu.vn.myfoody;

public class Food {
    private Integer FoodId;
    private String FoodName;
    private Float Price;
    private byte[] Image;

    public Food(Integer foodId, String foodName, Float price, byte[] image) {
        this.FoodId = foodId;
        this.FoodName = foodName;
        this.Price = price;
        this.Image = image;
    }

    public Integer getFoodId() {
        return FoodId;
    }

    public void setFoodId(Integer foodId) {
        FoodId = foodId;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public Float getPrice() {
        return Price;
    }

    public void setPrice(Float price) {
        Price = price;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }
}
