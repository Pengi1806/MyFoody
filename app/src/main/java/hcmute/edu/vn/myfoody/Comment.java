package hcmute.edu.vn.myfoody;

public class Comment {
    Integer CommentId;
    String CommentContent;
    Integer StoreId;
    String Email;

    public Comment(Integer commentId, String commentContent, Integer storeId, String email) {
        CommentId = commentId;
        CommentContent = commentContent;
        StoreId = storeId;
        Email = email;
    }

    public Integer getCommentId() {
        return CommentId;
    }

    public void setCommentId(Integer commentId) {
        CommentId = commentId;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String commentContent) {
        CommentContent = commentContent;
    }

    public Integer getStoreId() {
        return StoreId;
    }

    public void setStoreId(Integer storeId) {
        StoreId = storeId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
