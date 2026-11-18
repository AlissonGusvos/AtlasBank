package com.atlasbk.Atlas.Account;

import com.atlasbk.Atlas.Account.responses.OverviewResponse;
import com.atlasbk.Atlas.User.User;
import com.atlasbk.Atlas.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // MAIN MENU
    public OverviewResponse overview(Authentication authentication){

        Optional<User> optionalUser = userRepository.findByCpf(authentication.getName());
        if (optionalUser.isEmpty()){
            new Exception("Conta não encontrada! Tente novamente mais tarde");
        }

        User user = optionalUser.get();

        Optional<Account> optionalAccount = accountRepository.findByUserId_Id(user.getId());
        BigDecimal accountBalance = optionalAccount.get().getBalance();


        return new OverviewResponse(user.getUsername(), accountBalance);
    }

}
