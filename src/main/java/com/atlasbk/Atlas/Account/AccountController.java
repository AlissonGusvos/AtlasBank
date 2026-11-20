package com.atlasbk.Atlas.Account;

import com.atlasbk.Atlas.Account.request.DepositRequest;
import com.atlasbk.Atlas.Account.request.WithdrawRequest;
import com.atlasbk.Atlas.Account.responses.OverviewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    public OverviewResponse overview(Authentication authentication){
        return accountService.overview(authentication);
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request, Authentication authentication){
        return accountService.deposit(request, authentication);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest request, Authentication authentication){
        return accountService.withdraw(request, authentication);
    }
}
