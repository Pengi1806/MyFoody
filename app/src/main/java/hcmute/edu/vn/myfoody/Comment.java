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
}
