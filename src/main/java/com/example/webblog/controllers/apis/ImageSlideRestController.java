package com.example.webblog.controllers.apis;

import com.example.webblog.models.ImageSlide;
import com.example.webblog.requestmodel.PostRequest;
import com.example.webblog.servies.imageslide.IImageSlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/image-slides")
public class ImageSlideRestController {

    @Autowired
    private IImageSlideService imageSlideService;

    @Autowired
    private ServletContext servletContext;

    @GetMapping
    public ResponseEntity<List<ImageSlide>> getImageSlides() {
        return ResponseEntity.ok((List<ImageSlide>) imageSlideService.findAll());
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ImageSlide> getImageSlideById(@PathVariable("imageId") Long imageId) {
        // Lấy thông tin ImageSlide dựa trên imageId từ service
        Optional<ImageSlide> imageSlide = imageSlideService.findById(imageId);
        // Kiểm tra xem ImageSlide có tồn tại hay không
        if (imageSlide.isPresent()) {
            // Nếu tồn tại, trả về thông tin ImageSlide và HTTP
            return ResponseEntity.ok(imageSlide.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ImageSlide> createImageSlide(@ModelAttribute ImageSlide imageSlide,
                                                       @RequestParam("imgFile") MultipartFile picture)
    { // Thực hiện upload hình ảnh và lưu đường dẫn vào ImageSlide
        uploadImage(picture);
        imageSlide.setLink("/img-upload/" + StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename())));
        return ResponseEntity.ok(imageSlideService.save(imageSlide));
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<ImageSlide> updateImageSlide(@PathVariable Long imageId,
               @ModelAttribute ImageSlide updatedImageSlide,
               @RequestParam("imgFile") MultipartFile picture) {

        // Lấy thông tin ImageSlide dựa trên imageId từ service
        Optional<ImageSlide> imageSlide = imageSlideService.findById(imageId);
        // Kiểm tra xem ImageSlide có tồn tại hay không
        if (imageSlide.isPresent()) {
            uploadImage(picture);
            updatedImageSlide.setLink("/img-upload/" + StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename())));

            ImageSlide existingImageSlide = imageSlide.get();
            existingImageSlide.setUrl(updatedImageSlide.getUrl());
            existingImageSlide.setLink(updatedImageSlide.getLink());
            existingImageSlide.setDescription(updatedImageSlide.getDescription());
            return ResponseEntity.ok(imageSlideService.save(existingImageSlide));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-no-img/{imageId}")
    public ResponseEntity<ImageSlide> updateImageSlideWithoutImg(@PathVariable Long imageId,
                                                       @ModelAttribute ImageSlide updatedImageSlide) {

        Optional<ImageSlide> imageSlide = imageSlideService.findById(imageId);
        // Kiểm tra xem ImageSlide có tồn tại hay không
        if (imageSlide.isPresent()) {

            // Cập nhật thông tin ImageSlide mà không cần upload hình ảnh mới
            ImageSlide existingImageSlide = imageSlide.get();
            existingImageSlide.setDescription(updatedImageSlide.getDescription());
            return ResponseEntity.ok(imageSlideService.save(existingImageSlide));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{imageId}")
    public ResponseEntity<Boolean> deleteImageSlide(@PathVariable Long imageId) {
        boolean isDeleted = imageSlideService.remove(imageId);
        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public String uploadImage(MultipartFile picture){
        String img = "";
        if (!picture.isEmpty()) {
            try {
                // Lấy tên file từ hình ảnh
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));
                // Định nghĩa thư mục để lưu trữ file đã tải lên
                String uploadDir = "WEB-INF/static/img-upload";
                // Lấy đường dẫn gốc của ứng dụng web
                String rootPath = servletContext.getRealPath("/");
                // Tạo thư mục nếu nó không tồn tại
                Path dirPath = Paths.get(rootPath, uploadDir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }
                // Xây dựng đường dẫn lưu trữ file
                Path filePath = Paths.get(rootPath, uploadDir, fileName);

                // Lưu file vào thư mục đã chỉ định
                Files.copy(picture.getInputStream(), filePath);

                // Trả về đường dẫn của file đã lưu

                return "/img-upload/"+fileName;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }
}