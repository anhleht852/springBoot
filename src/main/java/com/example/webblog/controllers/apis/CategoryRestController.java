package com.example.webblog.controllers.apis;

import com.example.webblog.models.Category;
import com.example.webblog.servies.category.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/categories")
public class CategoryRestController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getCategories() {
        // Lấy danh sách tất cả các Category từ service và trả về trong response body
        return ResponseEntity.ok((List<Category>) categoryService.findAll());
    }

    // Endpoint GET: /api/categories/{categoryId}
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        // Lấy Category dựa trên categoryId từ service
        Optional<Category> category = categoryService.findById(categoryId);

        // Kiểm tra xem Category có tồn tại hay không
        if (category.isPresent()) {
            // Nếu tồn tại, trả về thông tin Category và HTTP 200 OK
            return ResponseEntity.ok(category.get());
        } else {
            // Nếu không tồn tại, trả về HTTP 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // Thiết lập ngày tạo mới cho Category
        category.setCreateAt(new Date());

        // Kiểm tra xem Category có tồn tại hay không dựa trên tên
        Category categoryExist = categoryService.getCategoryByName(category.getNameCategory());
        if (categoryExist != null) {
            // Nếu tồn tại, trả về HTTP 409 Conflict
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Nếu không tồn tại, lưu Category và trả về thông tin Category và HTTP 200 OK
        return ResponseEntity.ok(categoryService.save(category));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody Category categoryRequest) {
        // Lấy Category dựa trên categoryId từ service
        Optional<Category> category = categoryService.findById(categoryId);

        // Kiểm tra xem Category có tồn tại hay không
        if (category.isPresent()) {
            // Kiểm tra xem Category mới có trùng tên với Category khác hay không
            Category categoryExist = categoryService.getCategoryByName(categoryRequest.getNameCategory());
            if (categoryExist != null) {
                // Nếu trùng tên, trả về HTTP 409 Conflict
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            // Cập nhật thông tin Category và trả về thông tin mới và HTTP 200 OK
            category.get().setNameCategory(categoryRequest.getNameCategory());
            return ResponseEntity.ok(categoryService.save(category.get()));
        } else {
            // Nếu không tồn tại, trả về HTTP 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        // Thực hiện xóa Category dựa trên categoryId và trả về kết quả
        boolean isDeleted = categoryService.remove(categoryId);
        if (isDeleted) {
            // Nếu xóa thành công, trả về true và HTTP 200 OK
            return ResponseEntity.ok(true);
        } else {
            // Nếu không tồn tại, trả về HTTP 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}