package com.atlasbk.Atlas.Account;

import com.atlasbk.Atlas.Account.request.DepositRequest;
import com.atlasbk.Atlas.Account.request.TransferRequest;
import com.atlasbk.Atlas.Account.request.WithdrawRequest;
import com.atlasbk.Atlas.Account.responses.ExtractResponse;
import com.atlasbk.Atlas.Account.responses.OverviewResponse;
import com.atlasbk.Atlas.Extract.ExtractService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final ExtractService extractService;

    public AccountController(AccountService accountService, ExtractService extractService) {
        this.accountService = accountService;
        this.extractService = extractService;
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

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request, Authentication authentication){
        return accountService.transfer(request, authentication);
    }

    @GetMapping("/extract")
    public List<ExtractResponse> extract(Authentication authentication){
        return extractService.extractResponse(authentication);
    }
}
