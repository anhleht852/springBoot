package com.example.webblog.servies.comment;

import com.example.webblog.mappers.CommentMapper;
import com.example.webblog.models.Account;
import com.example.webblog.models.Comment;
import com.example.webblog.models.Post;
import com.example.webblog.repositories.AccountRepository;
import com.example.webblog.repositories.CommentRepository;
import com.example.webblog.repositories.PostRepository;
import com.example.webblog.requestmodel.CommentRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    //Annotation @SneakyThrows là một tính năng của thư viện Lombok trong Java,
    // giúp giả mạo checked exceptions mà không cần phải xử
    // lý chúng một cách chi tiết trong mã code.
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Iterable<CommentRequest> findAll() {
        List<Comment> comments = commentRepository.findAll();//Lấy tất cả các đối tượng Comment từ cơ sở dữ liệu
        List<CommentRequest> commentRequests = new ArrayList<>();//Tạo danh sách để chứa các đối tượng CommentRequest
        for (Comment comment : comments) {//Chuyển đổi từ đối tượng Comment sang CommentRequest và thêm vào danh sách
            commentRequests.add(CommentMapper.commentModelToCommentRequest(comment));
        }
        return commentRequests;//Trả về danh sách các đối tượng CommentRequest
    }

    @Override
    public Optional<CommentRequest> findById(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);//Tìm kiếm Comment trong cơ sở dữ liệu dựa trên id
        if (comment == null) {//Kiểm tra xem Comment có tồn tại hay không
            return Optional.empty();// Trả về Optional.empty() nếu không tìm thấy Comment
        }//Chuyển đổi từ đối tượng Comment sang CommentRequest
        CommentRequest commentRequest = CommentMapper.commentModelToCommentRequest(comment);
        return Optional.of(commentRequest);//Trả về Optional chứa CommentRequest
    }


    @SneakyThrows
    @Override
    public CommentRequest save(CommentRequest commentRequest) {
        //Tạo một đối tượng Comment mới hoặc lấy từ cơ sở dữ liệu nếu đã tồn tại
        Comment comment = new Comment();
        if (commentRequest.getCommentId() != null) {
            comment = commentRepository.findById(commentRequest.getCommentId()).orElse(null);
            if (comment != null) {
                comment.setContent(commentRequest.getContent());
            }
        } else {
            comment.setCommentAt(new Date());
        }
        //Thiết lập Post cho Comment nếu chưa có
        if (comment.getPost() == null) {
            Post post = postRepository.findById(commentRequest.getPostId()).get();
            comment.setPost(post);
        }
        //Thiết lập Account cho Comment nếu có phiên đăng nhập
        if (request.getSession().getAttribute("account-login") != null) {
            Account accountLogin = (Account) request.getSession().getAttribute("account-login");
            comment.setAccount(accountLogin);
        }
        //Thiết lập nội dung cho Comment
        comment.setContent(commentRequest.getContent());
        //Lưu trữ hoặc cập nhật Comment vào cơ sở dữ liệu
        Comment commentPost = commentRepository.save(comment);
        //Chuyển đổi từ Comment sang CommentRequest và trả về
        return CommentMapper.commentModelToCommentRequest(commentPost);
    }

    @Override
    public Boolean remove(Long id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override// sử dụng để lấy Comment dựa trên postId
    public List<CommentRequest> getCommentByPostId(Long postId) {
        List<Comment> comments = commentRepository.getAllByPostPostId(postId);
        List<CommentRequest> commentsRequests = new ArrayList<>();
        for (Comment comment : comments) {
            commentsRequests.add(CommentMapper.commentModelToCommentRequest(comment));
        }
        return commentsRequests;
    }

    @Override//xóa tất Comment liên quan đến một Post dựa trên postId
    public Boolean removeAllByPost(Long postId) {
        List<Comment> comments = commentRepository.getAllByPostPostId(postId);
        try {
            for (Comment comment : comments) {
                commentRepository.deleteById(comment.getCommentId());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override //xóa Comment liên quan đến một Account dựa trên accountId
    public Boolean removeAllByAccount(Long accountId) {
        List<Comment> comments = commentRepository.getAllByAccountAccountId(accountId);
        try {
            for (Comment comment : comments) {
                commentRepository.deleteById(comment.getCommentId());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}