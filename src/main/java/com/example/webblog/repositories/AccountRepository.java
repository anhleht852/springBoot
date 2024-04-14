package com.example.webblog.repositories;

import com.example.webblog.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    //phương thức như getAccountByEmailAndPassword, getAccountByEmail
    // để lấy tài khoản theo email và mật khẩu hoặc chỉ theo email.
    Account getAccountByEmailAndPassword(String email, String password);
    Account getAccountByEmail(String email);
}
