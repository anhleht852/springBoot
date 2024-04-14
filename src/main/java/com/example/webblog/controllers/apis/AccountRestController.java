package com.example.webblog.controllers.apis;

import com.example.webblog.models.Account;
import com.example.webblog.servies.account.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
public class AccountRestController {
    @Autowired
    private IAccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok((List<Account>) accountService.findAll());
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable("accountId") Long accountId) {
        // Lấy thông tin của một Account dựa trên accountId từ service
        Account account = accountService.findById(accountId).orElse(null);
        // Kiểm tra xem Account có tồn tại hay không
        if (account == null) {
            // Nếu không tồn tại, trả về HTTP 404 Not Found
            return ResponseEntity.notFound().build();
        }
        // Nếu tồn tại, trả về thông tin Account và HTTP 200 OK
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable("accountId") Long accountId, @RequestBody Account account){
        account.setAccountId(accountId); // Set accountId cho Account mới
        Account accountUpdate = accountService.save(account);
        return ResponseEntity.ok(accountUpdate);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Boolean> deleteAccount(@PathVariable("accountId") Long accountId){

        Account account = accountService.findById(accountId).orElse(null);
        if(account == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(accountService.remove(accountId));
    }
}
