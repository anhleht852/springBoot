package com.example.webblog.servies.account;

import com.example.webblog.models.Account;
import com.example.webblog.servies.IGeneralService;

import java.text.ParseException;
import java.util.Date;

public interface IAccountService extends IGeneralService<Account> {
//    Iterable<Account> findAll();// Lấy danh sách tất cả các tài khoản.
//
//    Optional<Account> findById(Long id);// Lấy một tài khoản dựa trên ID.
//
//    Account save(Account account);//: Lưu hoặc cập nhật thông tin của một tài khoản.
//
//    Boolean remove(Long id); // Xóa một tài khoản dựa trên ID. Trả về true nếu xóa thành công, ngược lại trả về false.

    ////
    Account login(String email, String password);//Đăng nhập và trả về thông tin tài khoản nếu email và mật khẩu đúng.

    Account getAccountByEmail(String email);//Lấy thông tin tài khoản dựa trên địa chỉ email.

    Boolean checkEmailExists(String email);//Kiểm tra xem có tài khoản nào đã đăng ký với địa chỉ email chưa.
    // Trả về true nếu email đã tồn tại, ngược lại trả về false.

    boolean register(String fullname, String email, String password, String birthday, int gender) throws ParseException;
    // Đăng ký một tài khoản mới với các thông tin cần thiết như tên, email, mật khẩu, ngày sinh, giới tính.
    // Trả về true nếu đăng ký thành công, ngược lại trả về false.
}