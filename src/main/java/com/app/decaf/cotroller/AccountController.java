package com.app.decaf.cotroller;

import com.app.decaf.model.Account;
import com.app.decaf.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.addAccount(account));
    }
}
