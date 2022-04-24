package hcmute.edu.vn.myfoody;

public class QuanAn {
    private int StoreId;
    private String StoreName;
    private String Address;
    private String OpenTime;
    private String CloseTime;
    private String Categories;
    private String Email;
    private float StoreRateStar;
    private byte[] CoverPhoto;

    public QuanAn(int storeId, String storeName, String address, String openTime, String closeTime, String categories, String email, float storeRateStar, byte[] coverPhoto) {
        StoreId = storeId;
        StoreName = storeName;
        Address = address;
        OpenTime = openTime;
        CloseTime = closeTime;
        Categories = categories;
        Email = email;
        StoreRateStar = storeRateStar;
        CoverPhoto = coverPhoto;
    }

    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int storeId) {
        StoreId = storeId;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOpenTime() {
        return OpenTime;
    }

    public void setOpenTime(String openTime) {
        OpenTime = openTime;
    }

    public String getCloseTime() {
        return CloseTime;
    }

    public void setCloseTime(String closeTime) {
        CloseTime = closeTime;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public float getStoreRateStar() {
        return StoreRateStar;
    }

    public void setStoreRateStar(float storeRateStar) {
        StoreRateStar = storeRateStar;
    }

    public byte[] getCoverPhoto() {
        return CoverPhoto;
    }

    public void setCoverPhoto(byte[] coverPhoto) {
        CoverPhoto = coverPhoto;
    }
}
