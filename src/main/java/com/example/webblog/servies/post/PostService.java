package com.example.webblog.servies.post;

import com.example.webblog.mappers.PostMapper;
import com.example.webblog.models.Account;
import com.example.webblog.models.Post;
import com.example.webblog.models.Rate;
import com.example.webblog.repositories.*;
import com.example.webblog.requestmodel.PostRequest;
import com.example.webblog.servies.comment.ICommentService;
import com.example.webblog.servies.rate.IRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IRateService rateService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    HttpServletRequest request;
    //HttpServletRequest cung cấp thông tin về request HTTP,sử dụng để truy cập thông tin của
    //phiên làm việc(session) như vai trò người dùng (user-role) và tài khoản đang đăng nhập (account-login).


    @Override//Trả về tất cả bài viết dưới dạng PostRequest
    @Cacheable(value = "allPosts")
    public Iterable<PostRequest> findAll() {//để lấy danh sách các bài viết
        List<PostRequest> posts = new ArrayList<>();
        for (Post post : postRepository.findAll()) {
            posts.add(PostMapper.postModelToPostRequest(post));//chuyển đổi từ mô hình Post sang đối tượng PostRequest.
        }
        return posts;
    }

    @Override//Tìm và trả về một bài viết dựa trên id nhất định dưới dạng PostRequest
    @Cacheable(value = "post", key = "#id")
    public Optional<PostRequest> findById(Long id) {//  lấy Optional<Post> từ cơ sở dữ liệu dựa trên id.
        Post post = postRepository.findById(id).orElse(null);;//Sử dụng orElse(null) để kiểm tra bài viết tồn tại không.
        if (post == null) return Optional.empty(); //Nếu không thì trả về Optional.empty().
        return Optional.of(PostMapper.postModelToPostRequest(post));//post tồn tại sử dụng
        // PostMapper.postModelToPostRequest(post) để chuyển đổi từ Post sang PostRequest.
    }


    @Override
    @CachePut(value = "post", key = "#post.postId")
    public PostRequest save(PostRequest post) {
        String role = (String) request.getSession().getAttribute("user-role");
        Post postModel = new Post();
        // Thiết lập các thuộc tính của bài viết từ PostRequest
        if(post.getPostId() != null) postModel.setPostId(post.getPostId());
        postModel.setTitle(post.getTitle());
        postModel.setContent(post.getContent());
        postModel.setCreateAt(new Date());
        postModel.setBriefContent(post.getBriefContent());
        postModel.setPicture(post.getPicture());
        postModel.setRate(0);

        // Thiết lập trạng thái của bài viết dựa trên vai trò của người dùng
        postModel.setStatus(role.equals("ROLE_ADMIN") ? 1 : 0);

        // Lấy thông tin tài khoản người đăng nhập từ session và gán vào bài viết
        Account account = (Account) request.getSession().getAttribute("account-login");
        postModel.setAccount(account);

        // Lấy thông tin Type từ TypeRepository và gán vào bài viết
        Optional<com.example.webblog.models.Type> type = typeRepository.findById(post.getTypeId());
        postModel.setType(type.orElse(null));

        // Lấy thông tin Category từ CategoryRepository và gán vào bài viết
        Optional<com.example.webblog.models.Category> category = categoryRepository.findById(post.getCategoryId());
        postModel.setCategory(category.orElse(null));

        // Lưu bài viết vào cơ sở dữ liệu và nhận lại bài viết đã được lưu
        Post postCreate = postRepository.save(postModel);
        // Chuyển đổi từ Post sang PostRequest và trả về
        return PostMapper.postModelToPostRequest(postCreate);
    }

    // Phương thức xóa bài viết và cập nhật cache
    @Override
    @Transactional
    @CacheEvict(value = {"allPosts", "post"}, allEntries = true)
    public Boolean remove(Long id) {
        try {
            commentService.removeAllByPost(id);
            rateService.removeAllByPost(id);
            postRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách bài viết dựa trên typeId và chuyển đổi chúng thành danh sách PostRequest
    @Override
    @Cacheable(value = "postsByType", key = "#typeId")
    public List<PostRequest> getAllPostByTypeId(Long typeId) {
        // Sử dụng postRepository để lấy danh sách bài viết từ cơ sở dữ liệu dựa trên typeId
        List<Post> posts = (List<Post>) postRepository.getAllByTypeTypeId(typeId);

        // Tạo danh sách để chứa các bài viết được chuyển đổi sang PostRequest
        List<PostRequest> postRequests = new ArrayList<>();

        // Duyệt qua danh sách bài viết và chuyển đổi từ Post sang PostRequest
        for (Post post : posts) {
            postRequests.add(PostMapper.postModelToPostRequest(post));
        }
        // Trả về danh sách bài viết dưới dạng PostRequest
        return postRequests;
    }

    // Lấy danh sách bài viết dựa trên categoryId và chuyển đổi chúng thành danh sách PostRequest
    @Override
    @Cacheable(value = "postsByCategory", key = "#categoryId")
    public List<PostRequest> getAllPostByCategoryId(Long categoryId) {
        // Sử dụng postRepository để lấy danh sách bài viết từ cơ sở dữ liệu dựa trên categoryId
        List<Post> posts = (List<Post>) postRepository.getAllByCategoryCategoryId(categoryId);

        // Tạo danh sách để chứa các bài viết được chuyển đổi sang PostRequest
        List<PostRequest> postRequests = new ArrayList<>();

        // Duyệt qua danh sách bài viết và chuyển đổi từ Post sang PostRequest
        for (Post post : posts) {
            postRequests.add(PostMapper.postModelToPostRequest(post));
        }
        // Trả về danh sách bài viết dưới dạng PostRequest
        return postRequests;
    }

    // Lấy danh sách bài viết dựa trên accountId và chuyển đổi chúng thành danh sách PostRequest
    @Override
    @Cacheable(value = "postsByAccount", key = "#accountId")
    public List<PostRequest> getAllPostByAccountId(Long accountId) {
        // Sử dụng postRepository để lấy danh sách bài viết từ cơ sở dữ liệu dựa trên accountId
        List<Post> posts = (List<Post>) postRepository.getAllByAccountAccountId(accountId);

        // Tạo danh sách để chứa các bài viết được chuyển đổi sang PostRequest
        List<PostRequest> postRequests = new ArrayList<>();

        // Duyệt qua danh sách bài viết và chuyển đổi từ Post sang PostRequest
        for (Post post : posts) {
            postRequests.add(PostMapper.postModelToPostRequest(post));
        }
        // Trả về danh sách bài viết dưới dạng PostRequest
        return postRequests;
    }

    // Lấy danh sách bài viết dựa trên title và chuyển đổi chúng thành danh sách PostRequest
    @Override
    @Cacheable(value = "postsByTitle", key = "#title")
    public List<PostRequest> getAllPostByTitle(String title) {
        // Sử dụng postRepository để lấy danh sách bài viết từ cơ sở dữ liệu dựa trên title
        List<Post> posts = (List<Post>) postRepository.getAllByTitleContaining(title);

        // Tạo danh sách để chứa các bài viết được chuyển đổi sang PostRequest
        List<PostRequest> postRequests = new ArrayList<>();

        // Duyệt qua danh sách bài viết và chuyển đổi từ Post sang PostRequest
        for (Post post : posts) {
            postRequests.add(PostMapper.postModelToPostRequest(post));
        }
        // Trả về danh sách bài viết dưới dạng PostRequest
        return postRequests;
    }



    @Override//lấy bài viết từ cơ sở dữ liệu dựa trên id.
    public Boolean activePost(Long id) {
        // Sử dụng postRepository để lấy bài viết từ cơ sở dữ liệu dựa trên id
        Post post = postRepository.findById(id).orElse(null);

        // Nếu bài viết tồn tại
        if (post != null) {
            post.setStatus(1);// Cập nhật trạng thái của bài viết thành 1 (kích hoạt)
            postRepository.save(post); // Lưu bài viết đã được cập nhật vào cơ sở dữ liệu
            return true;  // Trả về true thành công
        }
        return false;
    }


}