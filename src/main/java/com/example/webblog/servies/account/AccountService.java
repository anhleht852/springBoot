package com.example.webblog.servies.account;

import com.example.webblog.models.Account;
import com.example.webblog.models.Post;
import com.example.webblog.models.Rate;
import com.example.webblog.repositories.AccountRepository;
import com.example.webblog.repositories.PostRepository;
import com.example.webblog.repositories.RateRepository;
import com.example.webblog.servies.comment.ICommentService;
import com.example.webblog.servies.post.IPostService;
import com.example.webblog.servies.rate.IRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private IPostService postService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IRateService rateService;

    @Override
    public Iterable<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account save(Account account) {
        if (account.getAccountId() == null) {// Nếu là tài khoản mới, thêm mới vào cơ sở dữ liệu
            return accountRepository.save(account);
        } else {// Nếu là tài khoản đã tồn tại, cập nhật thông tin
            Account accountUpdate = accountRepository.findById(account.getAccountId()).orElse(null);
            if (accountUpdate == null) {
                return null;
            }
            // Cập nhật thông tin của tài khoản đã tồn tại
            accountUpdate.setFullname(account.getFullname());
            accountUpdate.setPassword(account.getPassword());
            accountUpdate.setEmail(account.getEmail());
            accountUpdate.setBirthday(account.getBirthday());
            accountUpdate.setGender(account.getGender());
            accountUpdate.setRole(account.getRole());
            // Lưu tài khoản đã cập nhật vào cơ sở dữ liệu
            return accountRepository.save(accountUpdate);
        }
    }

    @Override
    public Boolean remove(Long id) {
        try {
            // Lấy danh sách bài viết liên quan đến tài khoản
            List<Post> posts = (List<Post>) postRepository.getAllByAccountAccountId(id);
            // Xóa từng bài viết sử dụng postService
            for (Post post : posts) {
                postService.remove(post.getPostId());
            }
            commentService.removeAllByAccount(id);// Xóa tất cả comment liên quan đến tài khoản
            rateService.removeAllByAccount(id); // Xóa tất cả đánh giá liên quan đến tài khoản
            accountRepository.deleteById(id); // Xóa tài khoản chính thức khỏi cơ sở dữ liệu
            return true;   // Trả về true nếu quá trình xóa thành công
        } catch (Exception e) {
            e.printStackTrace();// In ra thông báo lỗi
            return false; // xóa không thành công
        }
    }

    @Override
    public Account login(String email, String password) {
        return accountRepository.getAccountByEmailAndPassword(email, password);
    }

    @Override //lấy thông tin tài khoản dựa trên địa chỉ email. Nếu không tìm thấy, nó trả về null.
    public Account getAccountByEmail(String email) {
        return accountRepository.getAccountByEmail(email);
    }

    @Override// kiểm tra email đã được đăng ký chưa trả về true nếu email tồn tại
    public Boolean checkEmailExists(String email) {
        Account account = accountRepository.getAccountByEmail(email);
        if (account == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean register(String fullname, String email, String password, String birthday, int gender) throws ParseException {
        //SimpleDateFormat để chuyển đổi chuỗi birthday thành một đối tượng Date để có thể lưu trữ trong đối tượng.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay = dateFormat.parse(birthday);
        Account account = new Account(fullname, password, email, birthDay, gender);// Tạo mới với thông tin đăng ký được cung cấp.
        Account accountRegister = save(account);
        if (accountRegister == null) {
            return false;
        }
        return true;
    }

}