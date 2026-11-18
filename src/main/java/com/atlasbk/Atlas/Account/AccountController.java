package com.atlasbk.Atlas.Account;

import com.atlasbk.Atlas.Account.responses.OverviewResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

}
