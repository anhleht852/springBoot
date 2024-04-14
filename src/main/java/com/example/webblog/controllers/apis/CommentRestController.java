package com.example.webblog.controllers.apis;

import com.example.webblog.mappers.CommentMapper;
import com.example.webblog.models.Post;
import com.example.webblog.requestmodel.CommentRequest;
import com.example.webblog.requestmodel.PostRequest;
import com.example.webblog.servies.comment.ICommentService;
import com.example.webblog.servies.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comments")
public class CommentRestController {
    @Autowired
    private ICommentService commentService;

    @Autowired
    private IPostService postService;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<CommentRequest>> getCommentByPostId(@PathVariable Long postId) {
        // Kiểm tra xem Post có tồn tại hay không dựa trên postId
        PostRequest postComment = postService.findById(postId).orElse(null);
        if(postComment == null){
            return ResponseEntity.notFound().build();
        }
        // Nếu tồn tại, lấy danh sách CommentRequest dựa trên postId và trả về trong response body
        List<CommentRequest> comments = commentService.getCommentByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<CommentRequest> createComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        // Kiểm tra xem Post có tồn tại hay không dựa trên postId
        PostRequest postComment = postService.findById(postId).orElse(null);

        // Nếu không tồn tại, trả về HTTP 404 Not Found
        if (postComment == null) {
            return ResponseEntity.notFound().build();
        }
        // Thiết lập postId cho CommentRequest và lưu vào cơ sở dữ liệu
        commentRequest.setPostId(postId);
        CommentRequest comment = commentService.save(commentRequest);
        // Trả về thông tin CommentRequest đã tạo và HTTP 200 OK
        return ResponseEntity.ok(comment);
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<CommentRequest> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentRequest comment = commentService.findById(commentId).orElse(null);
        if(comment == null){
            return ResponseEntity.notFound().build();
        }
        commentRequest.setCommentId(commentId);
        CommentRequest commentUpdate = commentService.save(commentRequest);
        return ResponseEntity.ok(commentUpdate);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable("commentId") Long commentId) {
        // Lấy thông tin CommentRequest dựa trên commentId từ service
        CommentRequest commentRequest = commentService.findById(commentId).orElse(null);
        // Kiểm tra xem CommentRequest có tồn tại hay không
        if (commentRequest == null) {
            // Nếu không tồn tại, trả về HTTP 404 Not Found
            return ResponseEntity.notFound().build();
        }
        // Thực hiện xóa CommentRequest dựa trên commentId và trả về kết quả
        boolean isDeleted = commentService.remove(commentId);
        // Trả về kết quả xóa và HTTP 200 OK nếu thành công, ngược lại trả về HTTP 404 Not Found
        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
