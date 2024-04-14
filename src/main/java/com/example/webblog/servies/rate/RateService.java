package com.example.webblog.servies.rate;

import com.example.webblog.mappers.RateMapper;
import com.example.webblog.models.Account;
import com.example.webblog.models.Post;
import com.example.webblog.models.Rate;
import com.example.webblog.models.RateId;
import com.example.webblog.repositories.AccountRepository;
import com.example.webblog.repositories.PostRepository;
import com.example.webblog.repositories.RateRepository;
import com.example.webblog.requestmodel.RateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RateService implements IRateService {

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    HttpServletRequest request;

    @Override
    public Iterable<RateRequest> findAll() {
        return null;
    }

    @Override
    public Optional<RateRequest> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public RateRequest save(RateRequest rateRequest) {
        Rate rate = new Rate(); //1: Tạo một đối tượng Rate
        Post post = postRepository.findById(rateRequest.getPostId()).get();//2: Lấy thông tin của bài viết mà đánh giá được thêm vào
        rate.setPost(post);
        //3: Lấy thông tin của tài khoản đăng nhập (đã lưu trong session)
        Account accountLogin = (Account) request.getSession().getAttribute("account-login");
        Account account = accountRepository.findById(accountLogin.getAccountId()).get();
        rate.setAccount(account);
        //4: Thiết lập giá trị đánh giá và tạo RateId
        rate.setValue(rateRequest.getValue());
        RateId rateId = new RateId(rateRequest.getPostId(), accountLogin.getAccountId());
        rate.setRateId(rateId);
        //5: Lưu đối tượng Rate vào cơ sở dữ liệu
        Rate ratePost = rateRepository.save(rate);
        //6: Trả về RateRequest thông qua RateMapper
        return RateMapper.rateModelToRateRequest(ratePost);
    }

    @Override
    public Boolean remove(Long id) {
        try {
            rateRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<RateRequest> getRateByPostId(Long postId) {
        List<Rate> rates = rateRepository.getAllByPostPostId(postId);//Lấy danh sách Đánh giá dựa trên ID Bài viết
        List<RateRequest> rateRequests = new ArrayList<>();//Khởi tạo danh sách để chứa các RateRequest
        // Duyệt qua danh sách Đánh giá và chuyển đổi sang RateRequest
        for (Rate rate : rates) {
            rateRequests.add(RateMapper.rateModelToRateRequest(rate));
        }
        return rateRequests;// Trả về danh sách RateRequest
    }

    //@Transactional được sử dụng để đảm bảo rằng phương thức này được thực hiện trong
    // một lần nếu lỗi nó sẽ bị rollback
    @Override
    @Transactional
    public Boolean removeAllByPost(Long postId) {
        try {
            rateRepository.deleteAllByPost_PostId(postId);//Xóa đánh giá dựa trên ID Bài viết
            return true;// xóa thành công
        } catch (Exception e) {// báo lỗi
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional // Xóa đánh giá dựa trên ID Tài khoản
    public Boolean removeAllByAccount(Long accountId) {
        try {
            rateRepository.deleteAllByAccount_AccountId(accountId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}