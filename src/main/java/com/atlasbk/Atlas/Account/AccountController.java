package com.atlasbk.Atlas.Account;

import com.atlasbk.Atlas.Account.request.DepositRequest;
import com.atlasbk.Atlas.Account.responses.OverviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account")
    public OverviewResponse overview(Authentication authentication){
        return accountService.overview(authentication);
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request, Authentication authentication){
        return accountService.deposit(request, authentication);
    }

}
