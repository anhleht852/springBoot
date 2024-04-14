package com.example.webblog.controllers.apis;

import com.example.webblog.models.Rate;
import com.example.webblog.requestmodel.PostRequest;
import com.example.webblog.requestmodel.RateRequest;
import com.example.webblog.servies.post.IPostService;
import com.example.webblog.servies.rate.IRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/rates")
public class RateRestController {

    @Autowired
    private IRateService rateService;

    @Autowired
    private IPostService postService;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<RateRequest>> getRateByPostId(@PathVariable Long postId) {
        List<RateRequest> rates = rateService.getRateByPostId(postId);
        return ResponseEntity.ok(rates);
    }

    // Phương thức xử lý yêu cầu HTTP POST hoặc PUT để tạo hoặc cập nhật xếp hạng cho một bài đăng
    @RequestMapping(value = "/posts/{postId}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<RateRequest> createOrUpdateRate(@PathVariable Long postId, @RequestBody RateRequest rateRequest) {
        PostRequest postRequest = postService.findById(postId).orElse(null);// Tìm bài đăng dựa trên id
        // Kiểm tra xem bài đăng có tồn tại không
        if(postRequest == null){
            // Trả về một ResponseEntity với trạng thái NOT_FOUND nếu bài đăng không tồn tại
            return ResponseEntity.notFound().build();
        }
        // Đặt id bài đăng cho đối tượng rateRequest
        rateRequest.setPostId(postId);
        // Lưu trữ xếp hạng bằng cách sử dụng rateService và nhận đối tượng xếp hạng đã lưu trữ
        RateRequest rate = rateService.save(rateRequest);
        // Trả về một ResponseEntity với đối tượng xếp hạng đã lưu trữ và trạng thái HTTP OK
        return ResponseEntity.ok(rate);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Boolean> deleteRate(@PathVariable("postId") Long postId) {
        boolean isDeleted = rateService.remove(postId);
        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
