package com.example.webblog.controllers.apis;

import com.example.webblog.models.Account;
import com.example.webblog.models.Category;
import com.example.webblog.models.Post;
import com.example.webblog.models.Type;
import com.example.webblog.requestmodel.PostRequest;
import com.example.webblog.servies.account.IAccountService;
import com.example.webblog.servies.category.ICategoryService;
import com.example.webblog.servies.post.IPostService;
import com.example.webblog.servies.type.ITypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/posts")
public class PostRestController {
    @Autowired
    private IPostService postService;

    @Autowired
    private ITypeService typeService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    @Cacheable(value = "allPosts")
    public ResponseEntity<List<PostRequest>> getPosts() {
        List<PostRequest> posts = (List<PostRequest>) postService.findAll();
        redisTemplate.opsForValue().set("allPosts", posts);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    @Cacheable(value = "postCache", key = "#postId")
    public ResponseEntity<PostRequest> getPostById(@PathVariable("postId") Long postId) {
        Optional<PostRequest> post = postService.findById(postId);
        // Kiểm tra xem  có tồn tại không
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



//    @GetMapping("/types/{typeId}")
//    @Cacheable(value = "typePostsCache", key = "#typeId")
//    public ResponseEntity<List<PostRequest>> getPostByTypeId(@PathVariable("typeId") Long typeId) {
//        Optional<Type> type = typeService.findById(typeId);
//        if (!type.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// Nếu không tồn tại
//        }
//        List<PostRequest> posts = postService.getAllPostByTypeId(typeId);
//        if (posts.isEmpty()) {// Kiểm tra xem có bài post
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);// Nếu không tồn tại
//        } else {
//            return ResponseEntity.ok(posts);
//        }
//    }
    @GetMapping("/types/{typeId}")
    @Cacheable(value = "typePostsCache", key = "#typeId")
    public ResponseEntity<List<PostRequest>> getPostByTypeId(@PathVariable("typeId") Long typeId) {
        List<PostRequest> posts = postService.getAllPostByTypeId(typeId);
        redisTemplate.opsForValue().set("type_" + typeId, posts);
        return ResponseEntity.ok(posts);
    }

//    @GetMapping("/categories/{categoryId}")
//    @Cacheable(value = "categoryPostsCache", key = "#categoryId")
//    public ResponseEntity<List<PostRequest>> getPostByCategoryId(@PathVariable("categoryId") Long categoryId) {
//        Optional<Category> category = categoryService.findById(categoryId);
//        if (!category.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        List<PostRequest> posts = postService.getAllPostByCategoryId(categoryId);
//        if (posts.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else {
//            return ResponseEntity.ok(posts);
//        }
//    }
    @GetMapping("/categories/{categoryId}")
    @Cacheable(value = "categoryPostsCache", key = "#categoryId")
    public ResponseEntity<List<PostRequest>> getPostByCategoryId(@PathVariable("categoryId") Long categoryId) {
        List<PostRequest> posts = postService.getAllPostByCategoryId(categoryId);
        redisTemplate.opsForValue().set("category_" + categoryId, posts);
        return ResponseEntity.ok(posts);
    }

//    @GetMapping("/search/{title}")
//    @Cacheable(value = "postsByTitle", key = "#title")
//    public ResponseEntity<List<PostRequest>> getAllPostByTitle(@PathVariable("title") String title) {
//        List<PostRequest> posts = postService.getAllPostByTitle(title);
//        if (posts.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else {
//            return ResponseEntity.ok(posts);
//        }
//    }

    @GetMapping("/search/{title}")
    @Cacheable(value = "postsByTitle", key = "#title")
    public ResponseEntity<List<PostRequest>> getAllPostByTitle(@PathVariable("title") String title) {
        List<PostRequest> posts = postService.getAllPostByTitle(title);
        redisTemplate.opsForValue().set("search_" + title, posts);
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/activePost/{id}")
    public ResponseEntity<Boolean> activePost(@PathVariable("id") Long id) {
        Boolean isSuccess = postService.activePost(id);
        if (!isSuccess) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(true);
        }
    }


//    @GetMapping("/accounts/{accountId}")
//    public ResponseEntity<List<PostRequest>> getPostByAccountId(@PathVariable("accountId") Long accountId) {
//        Optional<Account> account = accountService.findById(accountId);
//        if (!account.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        List<PostRequest> posts = postService.getAllPostByAccountId(accountId);
//        if (posts.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else {
//            return ResponseEntity.ok(posts);
//        }
//    }
    @GetMapping("/accounts/{accountId}")
    @Cacheable(value = "postsByAccount", key = "#accountId")
    public ResponseEntity<List<PostRequest>> getPostByAccountId(@PathVariable("accountId") Long accountId) {
        List<PostRequest> posts = postService.getAllPostByAccountId(accountId);
        redisTemplate.opsForValue().set("account_" + accountId, posts);
        return ResponseEntity.ok(posts);
    }


//    @PostMapping
//    public ResponseEntity<PostRequest> createPost(@ModelAttribute PostRequest postRequest,
//                                                  @RequestParam("pictureFile") MultipartFile picture) {
//        uploadImage(picture);
//        postRequest.setPicture("/img-upload/" + StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename())));
//        PostRequest createdPost = postService.save(postRequest);
//        return ResponseEntity.ok(createdPost);
//    }
    @PostMapping
    public ResponseEntity<PostRequest> createPost(@ModelAttribute PostRequest postRequest) {
        PostRequest createdPost = postService.save(postRequest);
        return ResponseEntity.ok(createdPost);
    }

//    @PutMapping("/{postId}")
//    public ResponseEntity<PostRequest> updatePost(@PathVariable("postId") Long postId, @ModelAttribute PostRequest postRequest,
//                                                  @RequestParam("pictureFile") MultipartFile picture) {
//        Optional<PostRequest> existingPost = postService.findById(postId); // Tìm bài đăng hiện tại dựa trên id
//        // Kiểm tra xem bài đăng có tồn tại không
//        if (existingPost.isPresent()) {
//            // Đặt id cho bài đăng mới
//            postRequest.setPostId(postId);
//
//            // Lấy đường dẫn hình ảnh từ bài đăng hiện tại và đặt nó trong bài đăng mới
//            postRequest.setPicture(existingPost.get().getPicture());
//
//            // Lưu trữ bài đăng đã cập nhật và nhận đối tượng đã cập nhật
//            PostRequest updatedPost = postService.save(postRequest);
//
//            // Trả về một ResponseEntity với bài đăng đã cập nhật và trạng thái HTTP OK
//            return ResponseEntity.ok(updatedPost);
//        } else {
//            // Trả về một ResponseEntity với trạng thái NOT_FOUND nếu bài đăng không tồn tại
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
    @PutMapping("/{postId}")
    public ResponseEntity<PostRequest> updatePost(@PathVariable("postId") Long postId, @ModelAttribute PostRequest postRequest) {
        Optional<PostRequest> existingPost = postService.findById(postId);
        if (existingPost.isPresent()) {
            postRequest.setPostId(postId);
            PostRequest updatedPost = postService.save(postRequest);
            return ResponseEntity.ok(updatedPost);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PutMapping("/update-no-picture/{postId}")
//    public ResponseEntity<PostRequest> updatePostWithoutPicture(@PathVariable("postId") Long postId,
//            @ModelAttribute PostRequest postRequest) {
//
//        Optional<PostRequest> existingPost = postService.findById(postId);
//        if (existingPost.isPresent()) {
//            postRequest.setPostId(postId);
//            postRequest.setPicture(existingPost.get().getPicture());
//            PostRequest updatedPost = postService.save(postRequest);
//            return ResponseEntity.ok(updatedPost);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
    @PutMapping("/update-no-picture/{postId}")
    public ResponseEntity<PostRequest> updatePostWithoutPicture(@PathVariable("postId") Long postId,
                                                                @ModelAttribute PostRequest postRequest) {
        Optional<PostRequest> existingPost = postService.findById(postId);
        if (existingPost.isPresent()) {
            postRequest.setPostId(postId);
            PostRequest updatedPost = postService.save(postRequest);
            return ResponseEntity.ok(updatedPost);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Boolean> deletePost(@PathVariable("postId") Long postId) {
        Optional<PostRequest> postRequest = postService.findById(postId);
        if (!postRequest.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        boolean isDeleted = postService.remove(postId);
        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public String uploadImage(MultipartFile picture){
        String img = "";
        // Kiểm tra xem tệp hình ảnh có được tải lên không
        if (!picture.isEmpty()) {
            try {
                // Lấy tên tệp hình ảnh và làm sạch nó
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));

                // Định nghĩa thư mục để lưu trữ tệp hình ảnh đã tải lên
                String uploadDir = "WEB-INF/static/img-upload";

                // Lấy đường dẫn gốc của ứng dụng web
                String rootPath = servletContext.getRealPath("/");

                // Tạo thư mục nếu nó chưa tồn tại
                Path dirPath = Paths.get(rootPath, uploadDir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }

                // Tạo đường dẫn lưu trữ cho tệp
                Path filePath = Paths.get(rootPath, uploadDir, fileName);

                // Lưu trữ tệp vào thư mục được chỉ định
                Files.copy(picture.getInputStream(), filePath);

                // Trả về đường dẫn hình ảnh sau khi đã lưu trữ
                return "/img-upload/" + fileName;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Trả về chuỗi rỗng nếu không có hình ảnh được tải lên
        return img;
    }
}