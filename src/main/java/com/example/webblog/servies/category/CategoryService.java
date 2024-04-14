package com.example.webblog.servies.category;

import com.example.webblog.models.Category;
import com.example.webblog.models.Post;
import com.example.webblog.repositories.CategoryRepository;
import com.example.webblog.repositories.PostRepository;
import com.example.webblog.servies.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {


    @Autowired
    private IPostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Boolean remove(Long id) {
        try { //  1: Lấy danh sách bài viết thuộc về danh mục
            List<Post> posts = (List<Post>) postRepository.getAllByCategoryCategoryId(id);

            //   2: Xóa từng bài viết trong danh mục
            for(Post post : posts){
                postService.remove(post.getPostId());
            }
            categoryRepository.deleteById(id); //   3: Xóa danh mục khỏi cơ sở dữ liệu
            return true; // Báo hiệu rằng quá trình xóa thành công
        } catch (Exception e) { //in thông báo lỗi ra console xóa không thành công
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByNameCategory(name);
    }
}